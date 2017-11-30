package com.irh.material.basics.netty.chapter02_3;

/**
 * Created by Iritchie.ren on 2017/2/22.
 *
 * This is NIO programing demo
 */
public class TimeServer{

    public static void main(String[] args){
        int port = 8000;

        if(args != null && args.length > 0){
            try{
                port = Integer.parseInt(args[0]);
            }catch(NumberFormatException nfe){
            }
        }

        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }

}
