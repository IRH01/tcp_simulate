package com.irh.material.basics.netty.chapter07_1;

import java.io.Serializable;

/**
 * Created by Iritchie.ren on 2017/3/1.
 */
public class SubscribeResp implements Serializable{

    private int subRespId;
    private int respCode;
    private String desc;

    public int getSubRespId(){
        return subRespId;
    }

    public void setSubRespId(int subRespId){
        this.subRespId = subRespId;
    }

    public int getRespCode(){
        return respCode;
    }

    public void setRespCode(int respCode){
        this.respCode = respCode;
    }

    public String getDesc(){
        return desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }
}
