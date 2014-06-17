package com.wangyin.cds.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class MasterNode extends ServerNode {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(MasterNode.class);
	private final class MasterInfo{
		EventLoopGroup bossGroup;
		EventLoopGroup workerGroup;
		int port;
	}
	private MasterInfo description;
	public MasterNode(){
		useInternalModule(Predefined.MODULE_ADMIN);
	}
	
	
	@Override
	protected InputStream getConfiguration() throws IOException{
		File file = new File(getHome());
		if (file.exists() && file.isDirectory()){
			return new FileInputStream(new File(file,"conf/master.conf"));
		}
		return null;
	}

	@Override
	protected void doInitialize(Map<String, Object> config) throws Exception {
		description = new MasterInfo();
		description.port = (Integer) config.get("port");
	}

	@Override
	protected void recieveEvent(NodeEvent event) {
		
	}

	@Override
	protected void doStartup() throws Exception {
		logger.info("try to start master on "+description.port);
		EventLoopGroup boss_group = new NioEventLoopGroup(1);
		EventLoopGroup worker_group = new NioEventLoopGroup();
		description.bossGroup = boss_group;
		description.workerGroup = worker_group;
		ServerBootstrap sbs = new ServerBootstrap();
		sbs.group(boss_group, worker_group);
		sbs.channel(NioServerSocketChannel.class).childHandler(new CdsProtobufInitializer(MasterHandler.class));
		Channel listen_channel = sbs.bind(description.port).sync().channel();
		listen_channel.closeFuture().sync();
	}

	@Override
	protected void doShutdown() throws Exception {
		if (description.bossGroup!= null){
			description.bossGroup.shutdownGracefully();
		}
		if (description.workerGroup!=null){
			description.workerGroup.shutdownGracefully();
		}
	}
}
