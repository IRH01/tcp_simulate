package com.irh.material.basics.netty.chapter08_2;

import com.irh.material.basics.netty.chapter08_1.SubscribeReqProto;
import com.irh.material.basics.netty.chapter08_1.SubscribeRespProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Iritchie.ren on 2017/3/1.
 */
public class SubReqServerHandler extends ChannelHandlerAdapter{
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
        if("renhuan".equalsIgnoreCase(req.getUserName())){
            System.out.println("Service accept client subscribe req : [\n" + req.toString() + "]");
            ctx.writeAndFlush(resp(req.getSubReqId()));
        }
    }
    
    private SubscribeRespProto.SubscribeResp resp(int subReqId){
        SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
        builder.setSubReqId(subReqId);
        builder.setRespCode(0);
        builder.setDesc("欢迎来到netty学习！");
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
