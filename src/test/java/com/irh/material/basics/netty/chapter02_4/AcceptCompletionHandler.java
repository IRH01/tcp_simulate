package com.irh.material.basics.netty.chapter02_4;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by Iritchie.ren on 2017/2/23.
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler>{

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment){
        attachment.asynchronousServerSocketChannel.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment){
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
