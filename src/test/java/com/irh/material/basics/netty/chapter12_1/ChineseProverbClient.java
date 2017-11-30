package com.irh.material.basics.netty.chapter12_1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * Created by Iritchie.ren on 2017/3/7.
 */
public class ChineseProverbClient {

    public void run(int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChineseProverbClientHandler());
            Channel ch = bootstrap.bind(0).sync().channel();
            ByteBuf buf = Unpooled.copiedBuffer("谚语字典查询？", CharsetUtil.UTF_8);
            ch.writeAndFlush(new DatagramPacket(buf, new InetSocketAddress("255.255.255.255", port))).sync();
            if (!ch.closeFuture().await(15000)) {
                System.out.println("查询超时！");
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8008;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {

            }
        }

        new ChineseProverbClient().run(port);
    }

}
