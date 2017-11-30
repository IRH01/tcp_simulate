package com.irh.material.basics.netty.chapter07_1;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Iritchie.ren on 2017/3/1.
 */
public class SubReqServerHandler extends ChannelHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        SubscribeReq req = (SubscribeReq) msg;
        if("renhuan".equalsIgnoreCase(req.getUserName())){
            System.out.println("Service accept client subscribe req : [" + JSONObject.toJSONString(req) + "]");
            ctx.writeAndFlush(resp(req.getSubReqId()));
        }
    }

    private SubscribeResp resp(int subReqId){
        SubscribeResp resp = new SubscribeResp();
        resp.setSubRespId(subReqId);
        resp.setRespCode(0);
        resp.setDesc("Netty book order succeed, 3 days later, send to the designated address");
        return resp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
