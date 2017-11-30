package com.irh.material.basics.netty.chapter06_1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Iritchie.ren on 2017/3/1.
 */
public class UserInfoTest{

    public static void main(String[] args) throws IOException{
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1);
        userInfo.setUserName("renhuan");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(userInfo);
        oos.flush();
        oos.close();
        byte[] bytes = baos.toByteArray();
        System.out.println("The jdk serializable length is : " + bytes.length);
        baos.close();
        System.out.println("--------------------------");
        System.out.println("The byte array serializable length is :" + userInfo.codeC().length);
    }
}
