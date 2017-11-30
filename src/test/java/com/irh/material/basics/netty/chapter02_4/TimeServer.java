package com.irh.material.basics.netty.chapter02_4;

/**
 * Created by Iritchie.ren on 2017/2/23.
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

        AsyncTimeServerHandler asyncServerHandler = new AsyncTimeServerHandler(port);

        new Thread(asyncServerHandler, "async time server handler").start();
    }
}
