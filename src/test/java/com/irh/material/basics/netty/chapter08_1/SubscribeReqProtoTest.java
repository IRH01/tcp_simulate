package com.irh.material.basics.netty.chapter08_1;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by Iritchie.ren on 2017/3/1.
 */
public class SubscribeReqProtoTest{

    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException{
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }


    public static void main(String[] args) throws InvalidProtocolBufferException{
        //构建google protocol对象
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqId(1);
        builder.setUserName("renhuan");
        builder.setProductName("netty book");
        builder.setAddress("湖南常德");
        SubscribeReqProto.SubscribeReq req = builder.build();

        System.err.println("Before encode : " + req.toString());
        //编码，输出二进制流
        byte[] encodeByteArray = req.toByteArray();
        System.err.println("After decode : " + encodeByteArray.toString());

        //解码，还原对象
        SubscribeReqProto.SubscribeReq req2 = SubscribeReqProto.SubscribeReq.parseFrom(encodeByteArray);
        System.out.println("After decode : " + req2.toString());
        System.out.println("Assert equal : --> " + req2.equals(req));
    }
}
