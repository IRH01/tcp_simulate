package com.irh.material.basics.netty.chapter07_1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Created by Iritchie.ren on 2017/3/1.
 */
public class SubReqClient{

    public void connect(String host, int port) throws InterruptedException{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(
                                    new ObjectDecoder(1024,
                                            ClassResolvers.cacheDisabled(this.getClass().getClassLoader()))
                            );
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new SubReqClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally{
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException{
        int port = 8008;
        if(args != null && args.length > 0){
            try{
                port = Integer.parseInt(args[0]);
            } catch(NumberFormatException nfe){

            }
        }
        new SubReqClient().connect("127.0.0.1", port);
    }

}
