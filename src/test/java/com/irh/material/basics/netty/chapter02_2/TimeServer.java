package com.irh.material.basics.netty.chapter02_2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Iritchie.ren on 2017/2/22
 * 同步阻塞IO
 */
public class TimeServer{

    public static void main(String[] args){
        int port = 9100;

        if(args != null && args.length > 0){
            try{
                port = Integer.parseInt(args[0]);
            }catch(NumberFormatException nfe){
            }
        }

        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("This time server is start in port :" + port);
            Socket socket;
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 10000);
            while(true){
                System.out.println("wait client connection join!vv");
                socket = serverSocket.accept();
                System.out.println("client connection join!");
                singleExecutor.executor(new TimeServerHandler(socket));
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(serverSocket != null){
                System.out.println("This time server close!vv");
                try{
                    serverSocket.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
