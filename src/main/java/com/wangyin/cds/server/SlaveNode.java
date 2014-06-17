package com.wangyin.cds.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SlaveNode extends ServerNode {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SlaveNode.class);
	private static final class SlaveDescription{
		EventLoop event;
		String master;
		int port;
	}
	private SlaveDescription description = new SlaveDescription();
	@Override
	protected InputStream getConfiguration() throws IOException {
		File file = new File(getHome());
		if (file.exists() && file.isDirectory()){
			return new FileInputStream(new File(file,"conf/slave.conf"));
		}
		return null;
	}

	@Override
	protected void doInitialize(Map<String, Object> config) throws Exception {
		description.master = (String) config.get("master_ip");
		description.port = (Integer) config.get("master_port");
	}

	@Override
	protected void recieveEvent(NodeEvent event) {
				
	}

	@Override
	protected void doStartup() throws Exception {
		logger.info("try to connect to master '"+description.master+":"+description.port);
		EventLoopGroup processor = new NioEventLoopGroup(2);
		Bootstrap boot = new Bootstrap();
		Channel channel = boot.group(processor).channel(NioSocketChannel.class).connect(description.master, description.port).sync().channel();
		channel.closeFuture().sync();
	}

	@Override
	protected void doShutdown() throws Exception {
		if(description.event != null){
			description.event.shutdownGracefully();
		}
	}

}
