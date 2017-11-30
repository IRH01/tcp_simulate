package com.irh.material.basics.netty.chapter11_1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;

/**
 * Created by Iritchie.ren on 2017/3/7.
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object>{

    private static final Logger LOGGER = Logger.getLogger(WebSocketServerHandler.class.getName());
    private WebSocketServerHandshaker handshaker;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception{
        if(msg instanceof FullHttpRequest){
            handlerHttpRequest(ctx, (FullHttpRequest) msg);
        } else if(msg instanceof WebSocketFrame){
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        if(frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if(frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content()).retain());
            return;
        }
        if(!(frame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }
        String request = ((TextWebSocketFrame) frame).text();
        if(LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine(String.format("%s received %s", ctx.channel(), request));
        }
        ctx.channel().write(new TextWebSocketFrame(request + " ,欢迎使用Netty WebSocket服务，现在时刻： " + new Date().toString()));
        return;
    }

    private void handlerHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req){
        if(!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))){
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8008/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if(handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else{
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse resp){
        if(resp.getStatus().code() != 200){
            ByteBuf buf = Unpooled.copiedBuffer(resp.getStatus().toString(), CharsetUtil.UTF_8);
            resp.content().writeBytes(buf);
            buf.release();
            setContentLength(resp, resp.content().readableBytes());
        }
        ChannelFuture future = ctx.channel().writeAndFlush(resp);
        if(!isKeepAlive(req) || resp.getStatus().code() != 200){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
