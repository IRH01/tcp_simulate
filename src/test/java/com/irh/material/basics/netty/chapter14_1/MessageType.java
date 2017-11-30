package com.irh.material.basics.netty.chapter14_1;

/**
 * Created by iritchie on 17-3-10.
 */
public enum MessageType{

    SERVICE_REQ((byte) 0),
    SERVICE_RESP((byte) 1),
    ONE_WAY((byte) 2),
    /**
     * 握手请求消息
     */
    LOGIN_REQ((byte) 3),
    LOGIN_RESP((byte) 4),
    HEARTBEAT_REQ((byte) 5),
    HEARTBEAT_RESP((byte) 6);

    private byte value;

    private MessageType(byte value){
        this.value = value;
    }

    public byte value(){
        return this.value;
    }
}
