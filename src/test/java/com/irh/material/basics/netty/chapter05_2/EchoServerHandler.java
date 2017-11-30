package com.irh.material.basics.netty.chapter05_2;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Iritchie.ren on 2017/2/28.
 */
public class EchoServerHandler extends ChannelHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        System.out.println("Receive client : [" + msg + "]");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
