/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.monitor;

import com.ambimmort.udserver.core.UdServer;

/**
 *
 * @author 定巍
 */
public class RuntimeMonitor {

    private static RuntimeMonitor instance = null;

    private UdServer server = null;


    private RuntimeMonitor() {
    }

    public void setServer(UdServer server) {
        this.server = server;
    }
    
    

    public static RuntimeMonitor getInstance() {
        if (instance == null) {
            instance = new RuntimeMonitor();
        }
        return instance;
    }
}
