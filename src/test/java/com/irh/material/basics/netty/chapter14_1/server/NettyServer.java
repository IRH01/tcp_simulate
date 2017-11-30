/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.irh.material.basics.netty.chapter14_1.server;

import com.irh.material.basics.netty.chapter14_1.NettyConstant;
import com.irh.material.basics.netty.chapter14_1.codec.NettyMessageDecoder;
import com.irh.material.basics.netty.chapter14_1.codec.NettyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.io.IOException;

/**
 * Created by iritchie on 17-3-10.
 */
public class NettyServer{

    public void bind() throws Exception{
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    public void initChannel(SocketChannel ch) throws IOException{
                        ch.pipeline().addLast("decoder", new NettyMessageDecoder(1024 * 1024, 4, 4));
                        ch.pipeline().addLast("encoder", new NettyMessageEncoder());
                        ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                        ch.pipeline().addLast("LoginAuthHandler", new LoginAuthRespHandler());
                        ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
                    }
                });

        // 绑定端口，同步等待成功
        bootstrap.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
        System.out.println("Netty server start ok : " + (NettyConstant.REMOTEIP + " : " + NettyConstant.PORT));
    }

    public static void main(String[] args) throws Exception{
        new NettyServer().bind();
    }
}
