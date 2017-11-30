
package com.irh.material.basics.netty.chapter14_1.client;

import com.irh.material.basics.netty.chapter14_1.struct.NettyMessage;
import com.irh.material.basics.netty.chapter14_1.MessageType;
import com.irh.material.basics.netty.chapter14_1.struct.Header;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by iritchie on 17-3-10.
 */
public class LoginAuthReqHandler extends ChannelHandlerAdapter{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        ctx.writeAndFlush(buildLoginReq());
    }

    private NettyMessage buildLoginReq(){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception{
        NettyMessage message = (NettyMessage) msg;
        // 如果是握手应答消息，需要判断是否认证成功
        if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
            Byte loginResult = (Byte) message.getBody();
            if(loginResult != (byte) 0){
                // 握手失败，关闭连接
                ctx.close();
            }else{
                System.out.println("Login is ok : " + message);
                ctx.fireChannelRead(msg);
            }
        }else
            ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        ctx.fireExceptionCaught(cause);
    }
}
