/**
 * Copyright(c) 2016 ShenZhen Gowild Intelligent Technology Co., Ltd. All rights reserved.
 * Created on 2016/9/11 15:01
 */
package com.irh.material.basics.netty.TCP.common;

/**
 * Created by Dream.xie on 2016/9/11.
 */
public class SocketInfo{
    /**
     *
     */
    private int port;
    /**
     *
     */
    private boolean isCiphertext;
    /**
     *
     */
    private int heartbeat;
    /**
     *
     */
    private String type;

    /**
     *
     */
    private boolean isNodeSocket = false;

    /**
     * getter port
     */
    public int getPort() {
        return port;
    }

    /**
     * setter port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * getter isCiphertext
     */
    public boolean isCiphertext() {
        return isCiphertext;
    }

    /**
     * setter isCiphertext
     */
    public void setCiphertext(boolean ciphertext) {
        isCiphertext = ciphertext;
    }

    /**
     * getter heartbeat
     */
    public int getHeartbeat() {
        return heartbeat;
    }

    /**
     * setter heartbeat
     */
    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    /**
     * getter type
     */
    public String getType() {
        return type;
    }

    /**
     * setter type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter isNodeSocket
     */
    public boolean isNodeSocket() {
        return isNodeSocket;
    }

    /**
     * setter isNodeSocket
     */
    public void setNodeSocket(boolean nodeSocket) {
        isNodeSocket = nodeSocket;
    }
}
