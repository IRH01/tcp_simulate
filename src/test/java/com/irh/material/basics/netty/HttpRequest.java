package com.irh.material.basics.netty;

import java.io.*;
import java.net.Socket;

public class HttpRequest{

    public static void main(String[] args) throws Exception{

        //连接到服务器    
        Socket socket = new Socket("localhost", 80);
        OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());

        StringBuffer header = new StringBuffer();
        header.append("GET / HTTP/1.1\r\n");
        header.append("Host:localhost\r\n");
        header.append("Accept-Language:zh-cn\r\n");
        //请求头结束  
        header.append("\r\n");
        out.write(header.toString());
        out.flush();

        InputStreamReader buf = new InputStreamReader(socket.getInputStream());
        int num = 0;
        FileOutputStream output = new FileOutputStream(new File("D:\\dev_data\\idea\\material\\src\\main\\webapp\\views\\index.html"));
        String str = getSockLine(buf);
        /** 解析出响应头部的所有字段 */
        while(!str.equals("\r\n")){
            if(str.contains("Content-Length:")){
                //解析出响应头部后真实的数据长度  
                num = Integer.parseInt(str.substring(15).trim());
            }
            str = getSockLine(buf);
        }
        char bytes[] = new char[1024];
        int nread = 0;

        while(num > 0){
            //Content-Length:字段是字节数长度。考虑汉字情况，如果直接用str.length处理。有可能会进入到死循环  
            nread = buf.read(bytes, 0, 1024);
            String temp = new String(bytes, 0, nread);
            output.write(temp.getBytes());
            //output.write(b, off, len)  
            //获取字符串字节长度  
            int len = temp.getBytes().length;
            num -= len;
            if(num <= 0) break;
        }
        output.flush();
        output.close();
        buf.close();
        socket.close();
    }

    private static String getSockLine(InputStreamReader buf) throws IOException{
        StringBuffer sb = new StringBuffer("");
        int n;
        while((n = buf.read()) != -1){
            sb.append((char) n);
            if(((char) n) == '\r'){
                n = buf.read();
                if(((char) n) == '\n'){
                    sb.append('\n');
                    break;
                }
            }
        }
        return sb.toString();
    }
}  