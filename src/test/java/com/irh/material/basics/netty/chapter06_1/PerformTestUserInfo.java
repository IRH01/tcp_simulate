package com.irh.material.basics.netty.chapter06_1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by Iritchie.ren on 2017/3/1.
 */
public class PerformTestUserInfo{

    public static void main(String[] args) throws IOException{
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(2);
        userInfo.setUserName("I.Ritchie.ren");
        int loop = 1000000;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < loop; i++){
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(userInfo);
            oos.flush();
            oos.close();
            byte[] bytes = baos.toByteArray();
            baos.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("The jdk serializable cost time is : " + (endTime - startTime) + "ms");
        System.out.println("-----------------------------");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        startTime = System.currentTimeMillis();
        for(int i = 0; i < loop; i++){
            byte[] bytes = userInfo.codeC();
        }
        endTime = System.currentTimeMillis();
        System.out.println("The byte Array serializable cost time is : " + (endTime - startTime) + "ms");
    }

}
