package com.irh.material.basics.netty.chapter02_3;

/**
 * Created by Iritchie.ren on 2017/2/22
 */
public class TimeClient{

    public static void main(String[] args){
        int port = 8000;

        if(args != null && args.length > 0){
            try{
                port = Integer.parseInt(args[0]);
            }catch(NumberFormatException nfe){
            }
        }

        new Thread(new TimeServerHandler("127.0.0.1", port), "Time client 002").start();
    }
}
