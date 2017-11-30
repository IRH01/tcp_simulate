package com.irh.material.basics.netty.chapter08_2;

import com.irh.material.basics.netty.chapter08_1.SubscribeReqProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Iritchie.ren on 2017/3/2.
 */
public class SubReqClientHandler extends ChannelHandlerAdapter{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        for(int i = 0; i < 10; i++){
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    private SubscribeReqProto.SubscribeReq subReq(int i){
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqId(i);
        builder.setUserName("renhuan");
        builder.setProductName("netty book");
        builder.setAddress("深圳市南山区125D");
        return builder.build();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        System.out.println("Receive server response : \n[" + msg + "]");
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
