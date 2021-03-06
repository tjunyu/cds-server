
package com.wangyin.cds.server.container;

import com.wangyin.cds.server.ServerNode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;

/**
 * @author wy
 */
public class FileUploadServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = Logger.getLogger(FileUploadServerHandler.class.getName());

    private HttpRequest request;

    private boolean readingChunks;

    private final StringBuilder responseContent = new StringBuilder();

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); //Disk
    // if
    // size
    // exceed

    private HttpPostRequestDecoder decoder;
    static {
        DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file
        // on exit (in normal
        // exit)
        DiskFileUpload.baseDirectory = null; // system temp directory
        DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on
        // exit (in normal exit)
        DiskAttribute.baseDirectory = null; // system temp directory
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if (decoder != null) {
            decoder.cleanFiles();
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
            HttpRequest request = this.request =  msg;
            URI uri = new URI(request.getUri());


            // if GET Method: should not try to create a HttpPostRequestDecoder
            try {
                decoder = new HttpPostRequestDecoder(factory, request);
            } catch (ErrorDataDecoderException e1) {
                e1.printStackTrace();
                responseContent.append(e1.getMessage());
                writeResponse(ctx.channel());
                ctx.channel().close();
                return;
            } catch (IncompatibleDataDecoderException e1) {
                // GET Method: should not try to create a HttpPostRequestDecoder
                // So OK but stop here
                responseContent.append(e1.getMessage());
                responseContent.append("\r\n\r\nEND OF GET CONTENT\r\n");
                writeResponse(ctx.channel());
                return;
            }

            readingChunks = HttpHeaders.isTransferEncodingChunked(request);
            responseContent.append("Is Chunked: " + readingChunks + "\r\n");
            responseContent.append("IsMultipart: " + decoder.isMultipart() + "\r\n");
            if (readingChunks) {//文件切块
                // Chunk version
                responseContent.append("Chunks: ");
                readingChunks = true;
            }


        // check if the decoder was constructed before
        // if not it handles the form get
        if (decoder != null) {
            if (msg instanceof HttpContent) {
                // New chunk is received
                HttpContent chunk = msg;
                try {
                    decoder.offer(chunk);
                } catch (ErrorDataDecoderException e1) {
                    e1.printStackTrace();
                    responseContent.append(e1.getMessage());
                    writeResponse(ctx.channel());
                    ctx.channel().close();
                    return;
                }
                responseContent.append('o');
                // example of reading chunk by chunk (minimize memory usage due to
                // Factory)
                readHttpDataChunkByChunk();
                // example of reading only if at the end
                if (chunk instanceof LastHttpContent) {
                    writeResponse(ctx.channel());
                    readingChunks = false;

                    reset();
                }
            }
        }
    }

    private void reset() {
        request = null;

        // destroy the decoder to release all resources
        decoder.destroy();
        decoder = null;
    }

    /**
     * Example of reading request by chunk and getting values from chunk to chunk
     */
    private void readHttpDataChunkByChunk() {
        try {
            Map<String,Object> values = new HashMap<String,Object>();
            while (decoder.hasNext()) {
                InterfaceHttpData data = decoder.next();
                if (data != null) {
                    try {
                        // new value
                        writeHttpData(data,values);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        data.release();
                    }
                }
            }
            //写文件
            File file = new File(ServerNode.http_config.get("scriptRoot")+ File.separator  );
        } catch (EndOfDataDecoderException e1) {
            // end
            responseContent.append("\r\n\r\nEND OF CONTENT CHUNK BY CHUNK\r\n\r\n");
        }
    }

    private void writeHttpData(InterfaceHttpData data,Map<String,Object> values) throws IOException {
        if (data.getHttpDataType() == HttpDataType.Attribute) {
            Attribute attribute = (Attribute) data;
            values.put(attribute.getName(),attribute.getValue());
            String value;
            try {
                value = attribute.getValue();
            } catch (IOException e1) {
                // Error while reading data from File, only print name and error
                e1.printStackTrace();
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": "
                        + attribute.getName() + " Error while reading value: " + e1.getMessage() + "\r\n");
                return;
            }
            if (value.length() > 100) {
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": "
                        + attribute.getName() + " data too long\r\n");
            } else {
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": "
                        + attribute.toString() + "\r\n");
            }
        } else {
            responseContent.append("\r\nBODY FileUpload: " + data.getHttpDataType().name() + ": " + data.toString()
                    + "\r\n");
            if (data.getHttpDataType() == HttpDataType.FileUpload) {
                FileUpload fileUpload = (FileUpload) data;
                if (fileUpload.isCompleted()) {
                    if (fileUpload.length() < 10000) {
                        responseContent.append("\tContent of file\r\n");
                        try {

                            values.put(fileUpload.getName(),fileUpload);
                            responseContent.append(fileUpload.getString(fileUpload.getCharset()));
                        } catch (IOException e1) {
                            // do nothing for the example
                            e1.printStackTrace();
                        }
                        responseContent.append("\r\n");
                    } else {
                        responseContent.append("\tFile too long to be printed out:" + fileUpload.length() + "\r\n");
                    }
                    // fileUpload.isInMemory();// tells if the file is in Memory
                    // or on File
                    // fileUpload.renameTo(dest); // enable to move into another
                    // File dest
                    // decoder.removeFileUploadFromClean(fileUpload); //remove
                    // the File of to delete file
                } else {
                    responseContent.append("\tFile to be continued but should not!\r\n");
                }
            }
        }
    }

    private void writeResponse(Channel channel) {
        // Convert the response content to a ChannelBuffer.
        ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
        responseContent.setLength(0);

        // Decide whether to close the connection or not.
        boolean close = HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.headers().get(CONNECTION))
                || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                && !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(request.headers().get(CONNECTION));

        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (!close) {
            // There's no need to add 'Content-Length' header
            // if this is the last response.
            response.headers().set(CONTENT_LENGTH, buf.readableBytes());
        }

        Set<Cookie> cookies;
        String value = request.headers().get(COOKIE);
        if (value == null) {
            cookies = Collections.emptySet();
        } else {
            cookies = CookieDecoder.decode(value);
        }
        if (!cookies.isEmpty()) {
            // Reset the cookies if necessary.
            for (Cookie cookie : cookies) {
                response.headers().add(SET_COOKIE, ServerCookieEncoder.encode(cookie));
            }
        }
        // Write the response.
        ChannelFuture future = channel.writeAndFlush(response);
        // Close the connection after the write operation is done if necessary.
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void writeMenu(ChannelHandlerContext ctx) {
        // print several HTML forms
        // Convert the response content to a ChannelBuffer.
        responseContent.setLength(0);

        // create Pseudo Menu
        responseContent.append("<html>");
        responseContent.append("<head>");
        responseContent.append("<title>Netty Test Form</title>\r\n");
        responseContent.append("</head>\r\n");
        responseContent.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");

        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr>");
        responseContent.append("<td>");
        responseContent.append("<h1>Netty Test Form</h1>");
        responseContent.append("Choose one FORM");
        responseContent.append("</td>");
        responseContent.append("</tr>");
        responseContent.append("</table>\r\n");

        // GET
        responseContent.append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<FORM ACTION=\"/formget\" METHOD=\"GET\">");
        responseContent.append("<input type=hidden name=getform value=\"GET\">");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        responseContent
                .append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        responseContent.append("</td></tr>");
        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseContent.append("</table></FORM>\r\n");
        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

        // POST
        responseContent.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<FORM ACTION=\"/formpost\" METHOD=\"POST\">");
        responseContent.append("<input type=hidden name=getform value=\"POST\">");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        responseContent
                .append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        responseContent.append("<tr><td>Fill with file (only file name will be transmitted): <br> "
                + "<input type=file name=\"myfile\">");
        responseContent.append("</td></tr>");
        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseContent.append("</table></FORM>\r\n");
        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

        // POST with enctype="multipart/form-data"
        responseContent.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
        responseContent.append("<input type=hidden name=getform value=\"POST\">");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        responseContent
                .append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        responseContent.append("<tr><td>Fill with file: <br> <input type=file name=\"myfile\">");
        responseContent.append("</td></tr>");
        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseContent.append("</table></FORM>\r\n");
        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

        responseContent.append("</body>");
        responseContent.append("</html>");

        ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, buf.readableBytes());

        // Write the response.
        ctx.channel().writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(Level.WARNING, responseContent.toString(), cause);
        ctx.channel().close();
    }
}
