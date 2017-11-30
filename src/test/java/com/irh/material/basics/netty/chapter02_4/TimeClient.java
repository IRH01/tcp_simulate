package com.irh.material.basics.netty.chapter02_4;

/**
 * Created by Iritchie.ren on 2017/2/24
 * NIO实现异步IO
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

        AsyncTimeClientHandler asyncTimeClientHandler = new AsyncTimeClientHandler("127.0.0.1", port);
        new Thread(asyncTimeClientHandler, "async time client 001").start();
    }
}
