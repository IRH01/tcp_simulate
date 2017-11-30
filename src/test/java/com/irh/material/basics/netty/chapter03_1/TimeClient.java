package com.irh.material.basics.netty.chapter03_1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Iritchie.ren on 2017/2/28.
 */
public class TimeClient{

    private void connect(String host, int port) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally{
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 8000;
        if(args != null && args.length > 0){
            try{
                port = Integer.parseInt(args[0]);
            } catch(NumberFormatException nfe){

            }
        }

        new TimeClient().connect("127.0.0.1", port);
    }

}
