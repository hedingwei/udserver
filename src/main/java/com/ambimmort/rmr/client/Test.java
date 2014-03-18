/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.rmr.client;

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
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumIntegerDigits(2);
        System.out.println(0x83);
        System.out.println((byte)0x83);
        System.out.println(Integer.toHexString(131));
        System.out.println(nf.format(Integer.parseInt(Integer.toHexString(131))));
    }
}
