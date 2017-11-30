package com.irh.material.basics.netty.chapter06_1;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by Iritchie.ren on 2017/3/1.
 */
public class UserInfo implements Serializable{

    private Integer userId;
    private String userName;

    public Integer getUserId(){
        return userId;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public byte[] codeC(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userId);
        buffer.flip();
        value = null;
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
