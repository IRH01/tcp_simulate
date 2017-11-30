package com.irh.material.basics.netty.chapter02_3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Iritchie.ren on 2017/2/22.
 */
public class MultiplexerTimeServer implements Runnable{

    private Selector selector;

    private ServerSocketChannel channel;
    //volatile防止重排序
    private volatile boolean stop;

    public MultiplexerTimeServer(int port){
        try{
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(port), 1024);

            this.selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The Time Server is start in port : " + port);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop = true;
    }

    @Override
    public void run(){
        while(!stop){
            try{
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key;
                while(it.hasNext()){
                    key = it.next();
                    it.remove();
                    this.handleInput(key);
                    if(key != null){
                        key.cancel();
                        if(key.channel() != null){
                            key.channel().close();
                        }
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        if(selector != null){
            try{
                selector.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    private void handleInput(SelectionKey key) throws IOException{
        if(key.isValid()){
            if(key.isAcceptable()){
                ServerSocketChannel server = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = server.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                SocketChannel channel = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readByte = channel.read(readBuffer);
                if(readByte > 0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("The time server receive order :" + body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                    this.doWrite(channel, currentTime);
                }else if(readByte < 0){
                    key.cancel();
                    channel.close();
                }else{

                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException{
        if(response != null && response.trim().length() > 0){
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }

}
