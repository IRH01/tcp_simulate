package com.irh.material.basics.netty.chapter02_3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Iritchie.ren on 2017/2/23.
 */
public class TimeServerHandler implements Runnable{

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeServerHandler(String host, int port){
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try{
            this.selector = Selector.open();
            this.socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
        try{
            this.doConnect();
        } catch(IOException e){
            e.printStackTrace();
        }
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
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        if(selector != null){
            try{
                selector.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    private void handleInput(SelectionKey key) throws IOException{
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if(key.isConnectable()){
            if(socketChannel.finishConnect()){
                socketChannel.register(selector, SelectionKey.OP_READ);
                this.doWrite(socketChannel);
            } else{
                System.exit(1);
            }
        }
        if(key.isReadable()){
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int readBytes = socketChannel.read(readBuffer);
            if(readBytes > 0){
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                String body = new String(bytes, "UTF-8");
                System.out.println("Now is " + body);
                this.stop = true;
            } else if(readBytes < 0){
                key.cancel();
                socketChannel.close();
            }
        }
    }

    private void doWrite(SocketChannel socketChannel) throws IOException{
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if(!writeBuffer.hasRemaining()){
            System.out.println("Send order 2 server succeed.");
        }
    }
    
    private void doConnect() throws IOException{
        if(socketChannel.connect(new InetSocketAddress(host, port))){
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else{
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

}
