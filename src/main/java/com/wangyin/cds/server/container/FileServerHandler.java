package com.wangyin.cds.server.container;

import static io.netty.handler.codec.http.HttpHeaders.Names.CACHE_CONTROL;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.DATE;
import static io.netty.handler.codec.http.HttpHeaders.Names.EXPIRES;
import static io.netty.handler.codec.http.HttpHeaders.Names.IF_MODIFIED_SINCE;
import static io.netty.handler.codec.http.HttpHeaders.Names.LAST_MODIFIED;
import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.METHOD_NOT_ALLOWED;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_MODIFIED;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import com.wangyin.cds.server.ServerNode;

/**   
 * @author wy   
 */
public class FileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

	 public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
	    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
	    public static final int HTTP_CACHE_SECONDS = 60;

	    @Override
	    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
	        if (!request.getDecoderResult().isSuccess()) {
	            sendError(ctx, BAD_REQUEST);
	            return;
	        }

	        if (request.getMethod() != GET) {
	            sendError(ctx, METHOD_NOT_ALLOWED);
	            return;
	        }

	        final String uri = request.getUri();
	        String path = sanitizeUri(uri);
	        if (path == null) {
	            sendError(ctx, FORBIDDEN);
	            return;
	        }

	        File file = new File(path);
	        if (file.isHidden() || !file.exists()) {
	            sendError(ctx, NOT_FOUND);
	            return;
	        }

	        if (file.isDirectory()) {
	            if (uri.endsWith("/")) {
	                sendListing(ctx, file);
	            } else {
	                sendRedirect(ctx, uri + '/');
	            }
	            return;
	        }

	        if (!file.isFile()) {
	            sendError(ctx, FORBIDDEN);
	            return;
	        }

	        // Cache Validation
	        String ifModifiedSince = request.headers().get(IF_MODIFIED_SINCE);
	        if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
	            SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
	            Date ifModifiedSinceDate = dateFormatter.parse(ifModifiedSince);

	            // Only compare up to the second because the datetime format we send to the client
	            // does not have milliseconds
	            long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
	            long fileLastModifiedSeconds = file.lastModified() / 1000;
	            if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds) {
	                sendNotModified(ctx);
	                return;
	            }
	        }

	        RandomAccessFile raf;
	        try {
	            raf = new RandomAccessFile(file, "r");
	        } catch (FileNotFoundException ignore) {
	            sendError(ctx, NOT_FOUND);
	            return;
	        }
	        long fileLength = raf.length();

	        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
	        HttpHeaders.setContentLength(response, fileLength);
	        setContentTypeHeader(response, file);
	        setDateAndCacheHeaders(response, file);
	        if (HttpHeaders.isKeepAlive(request)) {
	            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
	        }

	        // Write the initial line and the header.
	        ctx.write(response);

	        // Write the content
	        ChannelFuture sendFileFuture;
	        if (ctx.pipeline().get(SslHandler.class) == null) {
	            sendFileFuture =
	                    ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());
	        } else {
	            sendFileFuture =
	                    ctx.write(new HttpChunkedInput(new ChunkedFile(raf, 0, fileLength, 8192)),
	                            ctx.newProgressivePromise());
	        }

	        // Write the end marker
	        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

	        // Decide whether to close the connection or not.
	        if (!HttpHeaders.isKeepAlive(request)) {
	            // Close the connection when the whole content is written out.
	            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
	        }
	    }

	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	        cause.printStackTrace();
	        if (ctx.channel().isActive()) {
	            sendError(ctx, INTERNAL_SERVER_ERROR);
	        }
	    }

	    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

	    private static String sanitizeUri(String uri) {
	        // Decode the path.
	        try {
	            uri = URLDecoder.decode(uri, "UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            throw new Error(e);
	        }

	        if (!uri.startsWith("/")) {
	            return null;
	        }

	        uri = uri.replace("/script/", "");
	        
	        // Convert file separators.
	        uri = uri.replace('/', File.separatorChar);

	        // Simplistic dumb security check.
	        // You will have to do something serious in the production environment.
	        if (uri.contains(File.separator + '.') ||
	            uri.contains('.' + File.separator) ||
	            uri.startsWith(".") || uri.endsWith(".") ||
	            INSECURE_URI.matcher(uri).matches()) {
	            return null;
	        }

	        // Convert to absolute path.
//	        return SystemPropertyUtil.get("user.dir") + File.separator + uri;
	        return ServerNode.http_config.get("scriptRoot")+ File.separator + uri;
	    }

	    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

	    private static void sendListing(ChannelHandlerContext ctx, File dir) {
	        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
	        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");

	        StringBuilder buf = new StringBuilder();
	        String dirPath = dir.getPath();

	        buf.append("<!DOCTYPE html>\r\n");
	        buf.append("<html><head><title>");
	        buf.append("Listing of: ");
	        buf.append(dirPath);
	        buf.append("</title></head><body>\r\n");

	        buf.append("<h3>Listing of: ");
	        buf.append(dirPath);
	        buf.append("</h3>\r\n");

	        buf.append("<ul>");
	        buf.append("<li><a href=\"../\">..</a></li>\r\n");

	        for (File f: dir.listFiles()) {
	            if (f.isHidden() || !f.canRead()) {
	                continue;
	            }

	            String name = f.getName();
	            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
	                continue;
	            }

	            buf.append("<li><a href=\"");
	            buf.append(name);
	            buf.append("\">");
	            buf.append(name);
	            buf.append("</a></li>\r\n");
	        }

	        buf.append("</ul></body></html>\r\n");
	        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
	        response.content().writeBytes(buffer);
	        buffer.release();

	        // Close the connection as soon as the error message is sent.
	        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	    }

	    private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
	        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
	        response.headers().set(LOCATION, newUri);

	        // Close the connection as soon as the error message is sent.
	        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	    }

	    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
	        FullHttpResponse response = new DefaultFullHttpResponse(
	                HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
	        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

	        // Close the connection as soon as the error message is sent.
	        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	    }

	    /**
	     * When file timestamp is the same as what the browser is sending up, send a "304 Not Modified"
	     *
	     * @param ctx
	     *            Context
	     */
	    private static void sendNotModified(ChannelHandlerContext ctx) {
	        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_MODIFIED);
	        setDateHeader(response);

	        // Close the connection as soon as the error message is sent.
	        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	    }

	    /**
	     * Sets the Date header for the HTTP response
	     *
	     * @param response
	     *            HTTP response
	     */
	    private static void setDateHeader(FullHttpResponse response) {
	        SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
	        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

	        Calendar time = new GregorianCalendar();
	        response.headers().set(DATE, dateFormatter.format(time.getTime()));
	    }

	    /**
	     * Sets the Date and Cache headers for the HTTP Response
	     *
	     * @param response
	     *            HTTP response
	     * @param fileToCache
	     *            file to extract content type
	     */
	    private static void setDateAndCacheHeaders(HttpResponse response, File fileToCache) {
	        SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
	        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

	        // Date header
	        Calendar time = new GregorianCalendar();
	        response.headers().set(DATE, dateFormatter.format(time.getTime()));

	        // Add cache headers
	        time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
	        response.headers().set(EXPIRES, dateFormatter.format(time.getTime()));
	        response.headers().set(CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
	        response.headers().set(
	                LAST_MODIFIED, dateFormatter.format(new Date(fileToCache.lastModified())));
	    }

	    /**
	     * Sets the content type header for the HTTP Response
	     *
	     * @param response
	     *            HTTP response
	     * @param file
	     *            file to extract content type
	     */
	    private static void setContentTypeHeader(HttpResponse response, File file) {
	        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
	        response.headers().set(CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
	    }

}
