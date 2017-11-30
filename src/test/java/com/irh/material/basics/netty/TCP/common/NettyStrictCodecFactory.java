/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-2-24  下午3:02:17
 */
package com.irh.material.basics.netty.TCP.common;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;

/**
 * Netty 编码,解码器工厂.
 *
 * @author iritchie
 */
public final class NettyStrictCodecFactory{
    private static Logger log = Logger.getLogger(NettyStrictCodecFactory.class.getClass());

    /**
     * 解密密钥netty上下文属性
     */
    public static final AttributeKey<int[]> DECRYPTION_KEY = AttributeKey.valueOf("DECRYPTION_KEY");
    /**
     * 加密密钥netty上下文属性
     */
    public static final AttributeKey<int[]> ENCRYPTION_KEY = AttributeKey.valueOf("ENCRYPTION_KEY");

    /**
     *
     */
    private NettyStrictCodecFactory(){

    }

    /**
     * 获得编码器
     */
    public static MessageToByteEncoder getEncoder(final Boolean isCiphertext){
        MessageToByteEncoder encoder;
        if(isCiphertext){
            encoder = new NettyCiphertextStrictMessageEncoder();
        }else{
            encoder = new NettyPlaintextStrictMessageEncoder();
        }
        return encoder;
    }

    /**
     * 获得解码器
     */
    public static ByteToMessageDecoder getDecoder(final Boolean isCiphertext){
        ByteToMessageDecoder decoder;
        if(isCiphertext){
            decoder = new NettyCiphertextStrictMessageDecoder();
        }else{
            decoder = new NettyPlaintextStrictMessageDecoder();
        }
        return decoder;
    }
}
