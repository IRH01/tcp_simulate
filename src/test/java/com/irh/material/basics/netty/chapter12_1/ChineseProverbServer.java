package com.irh.material.basics.netty.chapter12_1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * Created by Iritchie.ren on 2017/3/7.
 */
public class ChineseProverbServer{

    public void run(int port) throws InterruptedException{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChineseProverbServerHandler());
            bootstrap.bind(port).sync().channel().closeFuture().await();
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
                nfe.printStackTrace();
            }
        }

        new ChineseProverbServer().run(port);
    }
}
