package com.irh.material.basics.netty.chapter12_1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * Created by Iritchie.ren on 2017/3/7.
 */
public class ChineseProverbClientHandler extends SimpleChannelInboundHandler<DatagramPacket>{

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception{
        String response = msg.content().toString(CharsetUtil.UTF_8);
        if(response.startsWith("谚语查询结果：")){
            System.out.println(response);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
