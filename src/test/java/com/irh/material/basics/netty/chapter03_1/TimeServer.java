package com.irh.material.basics.netty.chapter03_1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Iritchie.ren on 2017/2/24.
 */
public class TimeServer{

    public void bind(int port) throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    //用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
                    // 用于临时存放已完成三次握手的请求的队列的最大长度
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            System.err.print("");
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        }finally{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel ch) throws Exception{
            ch.pipeline().addLast(new TimeServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException{
        int port = 8000;
        if(args != null && args.length > 0){
            try{
                port = Integer.parseInt(args[0]);
            }catch(NumberFormatException nfe){
                System.err.println(nfe);
            }
        }
        new TimeServer().bind(port);
    }
}
