package com.wangyin.cds.server.container;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.ws.rs.core.Application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.wangyin.cds.server.Predefined;
import com.wangyin.cds.server.container.aa.ICdsSession;
import com.wangyin.cds.server.container.aa.RestAuth;
import com.wangyin.cds.server.container.aa.SessionHolder;
import com.wangyin.cds.server.container.aa.SessionManager;

/**   
 * @author wy   
 */
public class HttpDispatchHandler extends SimpleChannelInboundHandler<FullHttpRequest> implements
		ChannelHandler {

	private final int port;
	private FileServerHandler fileServerHandler;
	private MiniNettyContainer miniNettyContainer;
	private Map<String, Object> configuration;
	private SessionManager sessionManager;
	public HttpDispatchHandler(Map<String, Object> config){
		this.configuration = config;
		this.fileServerHandler = new FileServerHandler();
		this.miniNettyContainer = ContainerFactory.createContainer(MiniNettyContainer.class, initApplcation());
		this.port = (Integer) this.configuration.get("port");
		this.sessionManager = new SessionManager();
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

	private Application initApplcation() {
		ResourceConfig app = new ResourceConfig();
		this.configuration.put(HttpDispatchInitializer.PROP_APPLICATION, app);
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
		app.property(Predefined.PROP_SESSION_MGR, this.sessionManager);
		try {
			app.property(Predefined.PROP_BASE_URI, new URI("http://"+configuration.get("ip")+":"+port+"/rest"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return app;
	}
}
