package com.wangyin.cds.server.modules;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map;

public abstract class TcpServiceModule extends ServerModule {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TcpServiceModule.class);
	private String bindIp="0.0.0.0";
	private int port;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workGroup;
	public void configure(Map<String, Object> configuration) {
		Integer obj_port = (Integer) configuration.get("port");
		assert(obj_port != null);
		port = obj_port;
		String ip = (String) configuration.get("bind_ip");
		if (ip != null) bindIp = ip;
	}

	public void startup() throws Exception {
		logger.info("try to start tcp service '"+getName()+"' on "+bindIp+":"+port);
		bossGroup = new NioEventLoopGroup(1);
		workGroup = new NioEventLoopGroup();
		ServerBootstrap sbs = new ServerBootstrap();
		sbs.group(bossGroup, workGroup);
		sbs.channel(NioServerSocketChannel.class).childHandler(getInitializer());
		Channel listen_channel = sbs.bind(bindIp,port).sync().channel();
		listen_channel.closeFuture().sync();
	}

	public abstract ChannelHandler getInitializer();

	public void shutdown() throws Exception {
		if (bossGroup != null){
			bossGroup.shutdownGracefully();
		}
		if (workGroup != null){
			workGroup.shutdownGracefully();
		}
	}

}
