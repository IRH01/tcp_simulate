package com.irh.material.basics.netty.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * MINA、Netty、Twisted一起学系列
 * http://xxgblog.com/2014/08/15/mina-netty-twisted-1/
 * 实现的TCP服务器，在连接建立、接收到客户端传来的数据、连接关闭时，都会触发某个事件,例如接收到客户端传来的数据时，
 * MINA会触发事件调用messageReceived，Netty会调用channelRead，Twisted会调用dataReceived。编写代码时，
 * 只需要继承一个类并重写响应的方法即可。这就是event-driven事件驱动。
 */
public class TcpServer{

    public static void main(String[] args) throws IOException{
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setHandler(new TcpServerHandle());
        acceptor.bind(new InetSocketAddress(9100));
    }

}

class TcpServerHandle extends IoHandlerAdapter{

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception{
        cause.printStackTrace();
    }

    // 接收到新的数据
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception{

        // 接收客户端的数据
        IoBuffer ioBuffer = (IoBuffer) message;
        byte[] byteArray = new byte[ioBuffer.limit()];
        ioBuffer.get(byteArray, 0, ioBuffer.limit());
        System.out.println("messageReceived:" + new String(byteArray, "UTF-8"));

        // 发送到客户端
        byte[] responseByteArray = ("messageReceived:" + new String(byteArray, "UTF-8") + "你好").getBytes("UTF-8");
        IoBuffer responseIoBuffer = IoBuffer.allocate(responseByteArray.length);
        responseIoBuffer.put(responseByteArray);
        responseIoBuffer.flip();
        session.write(responseIoBuffer);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception{
        System.out.println("sessionCreated");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception{
        System.out.println("sessionClosed");
    }
}