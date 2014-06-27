package com.wangyin.cds.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.wangyin.cds.server.container.HttpDispatchInitializer;
import com.wangyin.cds.server.modules.INodeModule;
import com.wangyin.cds.server.modules.ServerModule;
import com.wangyin.cds.server.persistence.PersistenceManager;

/**
 * 抽象的服务器节点，用作Master或者Slave
 * 
 * @author david wy
 * 
 */
public abstract class ServerNode {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(ServerNode.class);
	private static final String PROP_HTTP_SERVER = "http_server";
	private static final Map<String, String> INTERNAL_MODULES = new HashMap<String, String>();
	public static Map<String, Object> http_config;
	static {
		INTERNAL_MODULES.put(Predefined.MODULE_ADMIN,
				"com.wangyin.cds.server.modules.admin.AdminModule");
	}
	protected String home;
	protected Map<String, INodeModule> modules = new HashMap<String, INodeModule>();
	private ExecutorService service;
	private HttpDispatchInitializer httpDispatchInitializer;
	private PersistenceManager persistenceManager;
	private final class ServerDescription{
		EventLoopGroup boss;
		EventLoopGroup handle;
	}
	private ServerDescription serverDescription = new ServerDescription();
	public void setHome(String home) {
		this.home = home;
	}

	public String getHome() {
		return home;
	}

	protected abstract InputStream getConfiguration() throws IOException;

	protected abstract void doInitialize(Map<String, Object> config)
			throws Exception;

	protected void initialize() throws Exception {
		persistenceManager = new PersistenceManager();
		service = Executors.newFixedThreadPool(modules.size() + 1);
		InputStream conf_stream = getConfiguration();
		if (conf_stream == null) {
			throw new IOException("Null configuration");
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		Map<String, Object> config = mapper.readValue(conf_stream, Map.class);
		//configure HTTP SERVER
		http_config = (Map<String, Object>) config.get(PROP_HTTP_SERVER);
		httpDispatchInitializer = new HttpDispatchInitializer();
		//configure MODULES
		for (INodeModule module : modules.values()) {
			Object config_value = config.get(module.getName());
			if (config_value != null && !(config_value instanceof Map)) {
				throw new Exception("Wrong configuration for module "
						+ module.getName());
			}
			module.configure((Map<String, Object>) config_value);
		}
		//configure NODE SELF
		doInitialize(config);
	}

	private void startInternalMiniServer() {
		logger.info("try to start internal http server on "+ServerNode.http_config.get("port")+" which provides restful service");
		 EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	     EventLoopGroup workerGroup = new NioEventLoopGroup();
	     final ServerBootstrap sb = new ServerBootstrap();
	     sb.group(bossGroup, workerGroup);
	     service.execute(new Runnable() {
			public void run() {
				sb.channel(NioServerSocketChannel.class)
	             .childHandler(httpDispatchInitializer);
				 try {
					Channel ch = sb.bind((Integer)ServerNode.http_config.get("port")).sync().channel();
					ch.closeFuture().sync();
				} catch (InterruptedException e) {
					logger.error("fail to start internal server on "+ServerNode.http_config.get("port"),e);
				}
			}
		});
	    serverDescription.boss = bossGroup;
	    serverDescription.handle = workerGroup;
	}

	protected final boolean useInternalModule(String moduleName) {
		if (modules.containsKey(moduleName)
				|| !INTERNAL_MODULES.containsKey(moduleName)) {
			return false;
		}
		Class<?> cls_module;
		try {
			cls_module = Class.forName(INTERNAL_MODULES.get(moduleName));
			ServerModule module = (ServerModule) cls_module.newInstance();
			module.setName(moduleName);
			modules.put(moduleName, module);
			return true;
		} catch (ClassNotFoundException e) {
			logger.warn("use internal module, notice error:", e);
		} catch (InstantiationException e) {
			logger.warn("use internal module, notice error:", e);
		} catch (IllegalAccessException e) {
			logger.warn("use internal module, notice error:", e);
		}
		return false;
	}

	protected void startup() throws Exception {
		int ex_count = 0;
		//Start HTTP
		startInternalMiniServer();
		//Start each module
		for (final INodeModule module : modules.values()) {
			service.execute(new Runnable() {
				public void run() {
					try {
						module.startup();
					} catch (Exception e) {
						logger.error("exception occured when starting module "+module.getName(),e);
					}
				}
			});
		}
		if (ex_count > 0){
			Exception e = new Exception(ex_count+" exceptions occured, rollback log to check ");
			throw e;
		}else{
			doStartup();
		}
	}

	protected void shutdown() throws Exception {
		stopMiniServer();
		for (final INodeModule module : modules.values()) {
			module.shutdown();
		}
		Thread.sleep(1000);
		service.shutdownNow();
		doShutdown();
		logger.warn("node is shutdown");
	}

	private void stopMiniServer() {
		if (serverDescription.boss != null)
			serverDescription.boss.shutdownGracefully();
		if (serverDescription.handle != null){
			serverDescription.handle.shutdownGracefully();
		}
	}

	protected abstract void recieveEvent(NodeEvent event);

	protected abstract void doStartup() throws Exception;
	protected abstract void doShutdown() throws Exception;
	public final void sendEvent(NodeEvent event) {
		for (INodeModule mod : modules.values()) {
			if (mod != event.getSource()) {
				mod.recieveEvent(event);
			}
		}
		if (event.getSource() != this) {
			this.recieveEvent(event);
		}
	}
}
