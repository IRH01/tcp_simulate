package com.irh.material.basics.netty.chapter05_1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by Iritchie.ren on 2017/2/28.
 */
public class EchoClient{

    public void connect(String host, int port) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception{
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally{
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 8008;
        if(args != null && args.length > 0){
            try{
                port = Integer.parseInt(args[0]);
            } catch(NumberFormatException nfe){

            }
        }

        new EchoClient().connect("127.0.0.1", port);
    }

}
