/**
 * Copyright(c) 2014 ShenZhen Gowild Intelligent Technology Co., Ltd.
 * All rights reserved.
 * Created on  2014-3-7  下午4:59:09
 */
package com.irh.material.basics.netty.TCP.common;

import java.util.List;

/**
 * socket接收器
 *
 * @author iritchie
 */
public abstract class SocketAcceptor{
    /**
     *
     */
    protected SocketInfo socketInfo;

    /**
     */
    public SocketAcceptor(SocketInfo socketInfo){
        this.socketInfo = socketInfo;
    }

    /**
     * 停止
     */
    protected abstract void stop() throws Exception;

    /**
     * 各自的SOCKET框架负责 实现startSocket这个方法
     */
    protected abstract void start() throws Exception;

    /**
     * @return
     */
    public List<Object> getAllConnections(){
        throw new UnsupportedOperationException("请重写这个方法:" + this.getClass().getName());
    }

    /**
     * 获得socketInfo
     */
    public SocketInfo getSocketInfo(){
        return socketInfo;
    }


}
