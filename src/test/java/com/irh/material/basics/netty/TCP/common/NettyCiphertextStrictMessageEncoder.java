/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.material.basics.netty.TCP.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;

/**
 * Netty编码器-密文.
 *
 * @author iritchie
 */
final class NettyCiphertextStrictMessageEncoder extends MessageToByteEncoder<Message>{
    private static Logger log = Logger.getLogger(NettyCiphertextStrictMessageEncoder.class.getClass());

    @Override
    protected void encode(final ChannelHandlerContext ctx, final Message message, final ByteBuf out) throws Exception{
        // 若存在不同线程给同一玩家发送数据的情况，因此加密过程需要同步处理
        byte[] plainText = message.toByteBuffer().array();
        //获取key
        int[] encryptKey = getKey(ctx);
        //加密过程
        byte[] cipherText = SocketUtil.encode(plainText, encryptKey);
        out.writeBytes(cipherText);
    }

    /**
     * 获取当前加解密密钥
     *
     * @return
     */
    private static int[] getKey(final ChannelHandlerContext ctx){
        return ctx.channel().attr(NettyStrictCodecFactory.ENCRYPTION_KEY).get();
    }
}
