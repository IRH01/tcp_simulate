package com.irh.material.basics.netty.chapter02_4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Iritchie.ren on 2017/2/23.
 */
public class AsyncTimeServerHandler implements Runnable{

    private int port;
    public CountDownLatch latch;
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AsyncTimeServerHandler(int port){
        this.port = port;
        try{
            this.asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in the port :" + port);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        this.latch = new CountDownLatch(1);
        this.doAccept();
        try{
            latch.await();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void doAccept(){
        asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
    }
}
