package com.irh.material.basics.netty.chapter02_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Iritchie.ren on 2017/2/22.
 *
 * The demo is BIO programing.
 */
public class TimeClient{

    public static void main(String[] args){
        int port = 9100;

        if(args != null && args.length > 0){
            try{
                port = Integer.parseInt(args[0]);
            }catch(NumberFormatException nfe){
            }
        }

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            socket = new Socket("127.0.0.1", port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("QUERY TIME ORDER");
            System.out.println("Send order 2 server succeed.vv");
            String resp = in.readLine();
            System.out.println("Now is : " + resp);
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(out != null){
                out.close();
            }
            if(in != null){
                try{
                    in.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            if(socket != null){
                try{
                    socket.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
