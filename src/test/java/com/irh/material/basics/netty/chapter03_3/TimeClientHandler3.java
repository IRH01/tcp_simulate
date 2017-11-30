package com.irh.material.basics.netty.chapter03_3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * Created by Iritchie.ren on 2017/2/28.
 * 粘包现象演示
 */
public class TimeClientHandler3 extends ChannelHandlerAdapter{

    private static final Logger logger = Logger.getLogger(TimeClientHandler3.class.getName());

    private byte[] req;

    private int counter;


    public TimeClientHandler3(){
        req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        ByteBuf message;
        for(int i = 0; i < 10; i++){
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        String body = (String) msg;
        System.out.println("Now is :" + body + " ;the counter is :" + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        logger.warning("Unexception exception from downstream" + cause.getMessage());
        ctx.close();
    }
}
