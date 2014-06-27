package com.wangyin.cds.server.container;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.Application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.wangyin.cds.server.Predefined;
import com.wangyin.cds.server.ServerNode;
import com.wangyin.cds.server.session.ICdsSession;
import com.wangyin.cds.server.session.SessionHolder;
import com.wangyin.cds.server.session.SessionManager;

/**   
 * @author wy   
 */
public class HttpDispatchHandler extends SimpleChannelInboundHandler<FullHttpRequest> implements
		ChannelHandler {

	private FileServerHandler fileServerHandler;
	private MiniNettyContainer miniNettyContainer;
	public HttpDispatchHandler(SessionManager sessionManager){
		this.fileServerHandler = new FileServerHandler();
		this.miniNettyContainer = ContainerFactory.createContainer(MiniNettyContainer.class, initApplcation(sessionManager));
	}

	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request)
			throws Exception {

		String uri = request.getUri();
		if(uri.contains("/rest/")){
			ctx.channel().pipeline().addLast("rest", miniNettyContainer);
		}else if(uri.contains("/script/")){
			ctx.channel().pipeline().addLast("script", fileServerHandler);
		}
		ctx.fireChannelRead(request);
	}

	private Application initApplcation(SessionManager sessionManager) {
		ResourceConfig app = new ResourceConfig();
		ServerNode.http_config.put(HttpDispatchInitializer.PROP_APPLICATION, app);
		app.packages("com.wangyin.cds.server.container.sample");
		app.packages("com.wangyin.cds.server.modules.monitor");
		app.register(ObjectMapperProvider.class);
		app.register(JacksonFeature.class);
        app.register(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(SessionHolder.class).to(ICdsSession.class);
			}
		});
//		app.register(RestAuth.class);
		app.property(Predefined.PROP_SESSION_MGR, sessionManager);
		try {
			app.property(Predefined.PROP_BASE_URI, new URI("http://"+ServerNode.http_config.get("ip")+":"+ServerNode.http_config.get("port")+"/rest"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return app;
	}
}
