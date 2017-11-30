package com.irh.material.basics.netty.chapter07_1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by Iritchie.ren on 2017/3/1.
 */
public class SubReqServer{

    public void bind(int port) throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(
                                    new ObjectDecoder(1024 * 1024,
                                            ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader()))
                            );
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new SubReqServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
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
        new SubReqServer().bind(port);
    }
}
