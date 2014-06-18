/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.core;

import com.ambimmort.rmr.client.Client;
import com.ambimmort.rmr.client.Connection;
import com.ambimmort.rmr.messages.commons.UdMessage;
import com.ambimmort.udserver.configuration.UdServerConfig;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author 定巍
 */
public class UdMessageProcessor implements Runnable {

    private List<UdRawMessage> msgs = null;

    private static HashMap<String, Client> clients = new HashMap<String, Client>();

    private static HashMap<Byte, String> routers_ud1 = new HashMap<Byte, String>();
    private static HashMap<Byte, String> routers_ud2 = new HashMap<Byte, String>();

    public static void init() {
        UdServerConfig config = UdServerConfig.getConfig();
        HashMap clusters = config.getRouter();
        for (Object k : clusters.keySet()) {
            Client client = new Client();
            List cps = (List) clusters.get(k);
            for (int i = 0; i < cps.size(); i++) {
                String str = (String) cps.get(i);
                String host = str.split(":")[0];
                int port = Integer.parseInt(str.split(":")[1]);
                new Connection(host, port, client);
            }
            client.connect();
            clients.put((String) k, client);
        }

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumIntegerDigits(2);
        for (int i = 0; i < 256; i++) {
            routers_ud1.put((byte) i, "p_01_" + (Integer.toHexString(i).length()==2?Integer.toHexString(i):""+Integer.toHexString(i)));
            routers_ud2.put((byte) i, "p_02_" + (Integer.toHexString(i).length()==2?Integer.toHexString(i):""+Integer.toHexString(i)));
        }
    }

    public UdMessageProcessor() {

    }

    public static HashMap<String, Client> getClients() {
        return clients;
    }
    
    

    public UdMessageProcessor(List<UdRawMessage> msgs) {
        this.msgs = msgs;
    }

    public void run() {
        UdMessage m = new UdMessage();
        for (UdRawMessage msg : msgs) {
            m.setType(msg.getPacketType());
            m.setSubtype(msg.getPacketSubType());
            m.setMsg(msg.getBytes());
            if (msg.getPacketType() == 0x01) {
                if (routers_ud1.containsKey(m.getSubtype())) {
                    clients.get(routers_ud1.get(m.getSubtype())).send(m);
                }
            } else if (msg.getPacketType() == 0x02) {
                if (routers_ud2.containsKey(m.getSubtype())) {
                    clients.get(routers_ud2.get(m.getSubtype())).send(m);
                }
            }
        }
    }

}
