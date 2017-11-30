package com.irh.material.basics.netty.chapter10_3.pojo;

public class Birthday{

    private String birthday;
    
    public Birthday(){
    }

    public Birthday(String birthday){
        super();
        this.birthday = birthday;
    }

    public String getBirthday(){
        return birthday;
    }

    public void setBirthday(String birthday){
        this.birthday = birthday;
    }
    
    @Override
    public String toString(){
        return this.birthday;
    }
}