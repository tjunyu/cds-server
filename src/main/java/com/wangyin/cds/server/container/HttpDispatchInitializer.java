package com.wangyin.cds.server.container;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**   
 * @author wy   
 */
public class HttpDispatchInitializer extends ChannelInitializer<SocketChannel> {
	
	public static final String PROP_APPLICATION="com.wangyin.cds.server.container.HttpDispatchHandler.application";
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestDecoder());		
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("handler", new HttpDispatchHandler());
        
	}

}
