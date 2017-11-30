package com.irh.material.basics.netty.chapter07_1;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Iritchie.ren on 2017/3/1.
 */
public class SubReqClientHandler extends ChannelHandlerAdapter{
    
    public SubReqClientHandler(){
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        for(int i = 0; i < 10; i++){
            ctx.write(subReq(i));
        }
        ctx.flush();
    }
    
    private SubscribeReq subReq(int i){
        SubscribeReq req = new SubscribeReq();
        req.setAddress("湖南省常德市安乡县");
        req.setPhoneNumber("18676823505");
        req.setProductName("netty 权威指南");
        req.setSubReqId(i);
        req.setUserName("renhuan");
        return req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        System.out.println("Receive server response : [" + JSONObject.toJSONString(msg) + "]");
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
