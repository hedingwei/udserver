/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.rmr.client;

import com.ambimmort.rmr.messages.commons.UdMessage;
import de.ruedigermoeller.serialization.FSTObjectOutput;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

/**
 *
 * @author 定巍
 */
public class Test {

    public static void main(String[] args) throws IOException {

//        Client client = new Client();
//        new ConnectionPoint("localhost", 9700, client);
//        client.connect();
//        try {
//            Thread.sleep(5000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////
//        SdMSG msg = new SdMSG();
//        msg.setTt("tts");
//        msg.setUid("12sdfd");
//        System.out.println("1=========");
//        System.out.println(client.broadcast(msg));
//
////
//        for (int i = 0; i < 10000; i++) {
//            msg = new SdMSG();
//            msg.setTt("tts");
//            msg.setUid("" + i);
//
//            client.broadcast(msg);
//        }
        FSTObjectOutput oos = new FSTObjectOutput(new FileOutputStream("C:\\Users\\定巍\\Downloads\\tttt4.data"));
        UdMessage udmessage = new UdMessage();
        udmessage.setType((byte) 1);
        udmessage.setSubtype((byte) 2);
        byte[] data = new byte[1];
        for (int i = 0; i < 1; i++) {
            data[i] = 1;
        }
        udmessage.setMsg(data);
        oos.writeObject(udmessage);
        udmessage = new UdMessage();
        udmessage.setType((byte) 1);
        udmessage.setSubtype((byte) 2);
        data = new byte[2];
        for (int i = 0; i < 2; i++) {
            data[i] = 1;
        }
        udmessage.setMsg(data);
        oos.writeObject(udmessage);
        udmessage = new UdMessage();
        udmessage.setType((byte) 1);
        udmessage.setSubtype((byte) 2);
        data = new byte[2];
        for (int i = 0; i < 2; i++) {
            data[i] = 1;
        }
        udmessage.setMsg(data);
        oos.writeObject(udmessage);
        oos.flush();
        oos.close();
    }
}
