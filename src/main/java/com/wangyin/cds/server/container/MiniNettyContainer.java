package com.wangyin.cds.server.container;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedInput;
import io.netty.handler.stream.ChunkedStream;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.message.internal.HttpDateFormat;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerResponseWriter;

import com.wangyin.cds.server.Predefined;

/**
 * HTTP 1.1 Full Request
 * 
 * @author david
 * 
 */
@Sharable
public class MiniNettyContainer extends SimpleChannelInboundHandler<FullHttpRequest> implements Container {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MiniNettyContainer.class);

	private final class NettyResponseWriter implements ContainerResponseWriter {
		private Channel activeChannel;
		private HttpResponse response;

		public NettyResponseWriter(Channel activeChannel) {
			super();
			this.activeChannel = activeChannel;
		}

		public OutputStream writeResponseStatusAndHeaders(long contentLength, ContainerResponse responseContext)
				throws ContainerException {
			
			boolean useChunkMode = contentLength == -1;
			if (!useChunkMode) {
				HttpHeaders.addHeader(response, HttpHeaders.Names.CONTENT_LENGTH, contentLength);
				response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(responseContext
						.getStatus()));
			}else{
				response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(responseContext.getStatus()));
			}
			// fill header
			for (Map.Entry<String, List<Object>> headerEntry : responseContext.getHeaders().entrySet()) {
				HttpHeaders.addHeader(response, headerEntry.getKey(), join(headerEntry.getValue(), ", "));
			}
			// chunk transferring..
			if (useChunkMode) {
				HttpHeaders.setKeepAlive(response, true);
				HttpHeaders.setTransferEncodingChunked(response);
				activeChannel.write(response);
				return new OutputStream() {
					
					@Override
					public void write(int b) throws IOException {
						ByteBuf buff = Unpooled.copiedBuffer(new byte[]{(byte) b});
						logger.debug("write entity :"+b);
						activeChannel.write(buff);
					}

					@Override
					public void write(byte[] b, int off, int len) throws IOException {
						ByteBuf buff = Unpooled.copiedBuffer(b,off,len);
						if(logger.isDebugEnabled()){
							String result = buff.toString(CharsetUtil.UTF_8);
							logger.debug("write entity: "+result);
						}
						activeChannel.write(buff);
					}

					
				};
			} else {
				return new ByteBufOutputStream(((HttpContent)response).content());
			}
		}

		private String join(List<Object> list, String delimiter) {
			final StringBuilder sb = new StringBuilder();
			String currentDelimiter = "";

			for (Object o : list) {
				sb.append(currentDelimiter);
				sb.append(o.toString());
				currentDelimiter = delimiter;
			}

			return sb.toString();
		}

		public boolean suspend(long timeOut, TimeUnit timeUnit, TimeoutHandler timeoutHandler) {
			logger.warn("suspend invoked");
			return false;
		}

		public void setSuspendTimeout(long timeOut, TimeUnit timeUnit) throws IllegalStateException {
			logger.warn("'setSuspendTimeout invoked");
		}

		public void commit() {
			if (activeChannel.isOpen()){
				ChannelFuture future;
				if (HttpHeaders.isTransferEncodingChunked(response)){
					//Last Trailer
					future = activeChannel.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
				}else{
					future = activeChannel.writeAndFlush(response);
				}
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}

		public void failure(Throwable error) {
			logger.error("Uncaught exception in transport layer. This is likely a bug, closing channel.", error);
			if (activeChannel.isOpen()) {
				if (activeChannel.isWritable()) {
					ByteBuf content = Unpooled.copiedBuffer(("Uncaught exception!" + error.getMessage())
							.getBytes(CharsetUtil.UTF_8));
					final DefaultFullHttpResponse internalServerResponse = new DefaultFullHttpResponse(
							HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, content);
					activeChannel.write(internalServerResponse).addListener(ChannelFutureListener.CLOSE);
				} else {
					activeChannel.close();
				}
			}
		}

		public boolean enableResponseBuffering() {
			return false;
		}

	}

	private final SecurityContext FAKE_CONTEXT = new SecurityContext() {

		public boolean isUserInRole(String role) {
			return false;
		}

		public boolean isSecure() {
			return false;
		}

		public Principal getUserPrincipal() {
			return null;
		}

		public String getAuthenticationScheme() {
			return null;
		}
	};

	private final ApplicationHandler appHandler;
	private final URI baseUri;

	public MiniNettyContainer(ApplicationHandler appHandler) {
		this.appHandler = appHandler;
		this.baseUri = (URI) this.appHandler.getConfiguration().getProperty(Predefined.PROP_BASE_URI);
	}

	public ResourceConfig getConfiguration() {
		return this.appHandler.getConfiguration();
	}

	public void reload() {
		throw new UnsupportedOperationException("reload ..., waitting...");
	}

	public void reload(ResourceConfig configuration) {
		throw new UnsupportedOperationException("reload ..., waitting...");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		logger.debug("process request from " + msg.getUri());
		URI requestUri = this.baseUri.resolve(msg.getUri());
		ContainerRequest crequest = new ContainerRequest(baseUri, requestUri, msg.getMethod().name(), FAKE_CONTEXT,
				new MapPropertiesDelegate());
		final SocketAddress remoteAddress = ctx.channel().remoteAddress();
		crequest.setProperty(Predefined.PROP_REMOTE_ADDRESS, remoteAddress);
		
		// Set Header
		final MultivaluedMap<String, String> incomingHeaders = crequest.getHeaders();
		for (Map.Entry<String, String> headerEntry : msg.headers()) {
			incomingHeaders.add(headerEntry.getKey(), headerEntry.getValue());
		}
		// Set Content
		crequest.setEntityStream(new ByteBufInputStream(msg.content()));
		// Set Date
		final Date responseDate = new Date();
		crequest.getHeaders().add(HttpHeaders.Names.DATE, HttpDateFormat.getPreferredDateFormat().format(responseDate));
		// Set Callback WRiter

		crequest.setWriter(new NettyResponseWriter(ctx.channel()));

		this.appHandler.handle(crequest);
	}

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

		/*
		 * String text =
		 * "<HTML><HEAD><TITLE>HELLO WORLD</TITLE></HEAD><BODY><H1>OH!NO NO NO !</BODY></HTML>"
		 * ; DefaultFullHttpResponse response = new
		 * DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
		 * HttpResponseStatus.OK,Unpooled.copiedBuffer(text,CharsetUtil.UTF_8));
		 * response.headers().set(HttpHeaders.Names.CONTENT_TYPE,
		 * "text/html; charset=UTF-8");
		 * ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		 */
	}

}
