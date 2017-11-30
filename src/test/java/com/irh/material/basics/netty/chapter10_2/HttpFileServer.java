package com.irh.material.basics.netty.chapter10_2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by Iritchie.ren on 2017/3/2.
 */
public class HttpFileServer{

    public void run(final int port, final String url) throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    });
            ChannelFuture future = bootstrap.bind("127.0.0.1", port).sync();
            System.out.println("HTTP 文件目录服务器启动，网址是 ： http://127.0.0.1:" + port + url);
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
                nfe.printStackTrace();
            }
        }

        String url = "/src/test/java/com/irh/material/netty/chapter10_2";
        if(args.length > 1){
            url = args[1];
        }
        new HttpFileServer().run(port, url);
    }

}
