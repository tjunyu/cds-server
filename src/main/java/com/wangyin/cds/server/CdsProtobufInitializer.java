package com.wangyin.cds.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

import com.wangyin.cds.server.protocol.NodeProtocol;

public class CdsProtobufInitializer extends ChannelInitializer<SocketChannel> {

	private Class<?extends ChannelHandler> clsHandler;
	public CdsProtobufInitializer(Class<?extends ChannelHandler> cls){
		this.clsHandler = cls;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pl = ch.pipeline();
		pl.addLast("frameEncoder", new LengthFieldPrepender(4));
		pl.addLast("protoEncoder", new ProtobufEncoder());
		pl.addLast("frameDecoder",
                 new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
		pl.addLast("decoder",new ProtobufDecoder(NodeProtocol.NResponse.getDefaultInstance()));
		pl.addLast("nodehandler",clsHandler.newInstance());
	}

}
