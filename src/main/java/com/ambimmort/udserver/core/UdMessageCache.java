/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.core;


import com.ambimmort.udserver.configuration.UdServerConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author 定巍
 */
public class UdMessageCache extends TimerTask {

    private static UdMessageCache instance = null;

    private List<UdRawMessage> msgs = new ArrayList<UdRawMessage>();

    private int limit = 1000;

    private long overtime = 10000;

    private ExecutorService service = null;

    private long lastUpdateTime = System.currentTimeMillis();

    private Timer timer = new Timer("UdMessageCacheTimer");

    private UdMessageCache() {
        UdServerConfig config = UdServerConfig.getConfig();
        this.overtime = config.getCache().getPeriod() * 1000;
        this.limit = config.getCache().getLimit();
        service = Executors.newFixedThreadPool(config.getCache().getThreads_max());
        timer.scheduleAtFixedRate(this, 0, config.getCache().getCheck_interval() * 1000);
    }

    public static UdMessageCache getInstance() {
        if (instance == null) {
            instance = new UdMessageCache();
        }
        return instance;
    }

    public void add(UdRawMessage msg) {
        msgs.add(msg);
        lastUpdateTime = System.currentTimeMillis();
        if (msgs.size() >= limit) {
            service.submit(new UdMessageProcessor(msgs));
            msgs = new ArrayList<UdRawMessage>();
        }
    }

    @Override
    public void run() {
        if ((System.currentTimeMillis() - lastUpdateTime) >= overtime) {
            if (msgs.isEmpty()) {
                return;
            }
            service.submit(new UdMessageProcessor(msgs));
            msgs = new ArrayList<UdRawMessage>();
        }
    }

}
