package com.irh.material.basics.netty.chapter04_2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * User: kangfoo-mac
 * Date: 14-7-20
 * Time: 下午3:06
 */
public class TimeClient{

    public void connect(int port, String host){
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception{
                        ch.pipeline().addLast(new TimeClientHandler());
                    }
                });

        try{
            // 发起异步连接操作
            ChannelFuture future = bootstrap.connect(host, port).sync();
            System.err.print("");
            // 等待客户端链路关闭。
            future.channel().closeFuture().sync();
        }catch(InterruptedException e){
            //To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }finally{
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args){
        int port = 8080;
        if(args != null && args.length > 0){
            try{
                port = Integer.valueOf(args[0]);
            }catch(NumberFormatException e){
                // TODO 请自行扩展.
            }
        }

        new TimeClient().connect(port, "localhost");
    }
}
