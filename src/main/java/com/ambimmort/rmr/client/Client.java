/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.rmr.client;

import com.google.common.hash.Hashing;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author 定巍
 */
public class Client {

    private ConsistentHash<Connection> connectionsPoints = null;

    private List<Connection> cps = null;

    private int count = 0;

    public List<Connection> getCps() {
        return cps;
    }

    public Client(List<Connection> cps) {
        this.cps = cps;
        connectionsPoints = new ConsistentHash<Connection>(Hashing.murmur3_128(), cps.size(), cps);
    }

    public Client() {
        this.cps = new ArrayList<Connection>();
        connectionsPoints = new ConsistentHash<Connection>(Hashing.murmur3_128(), cps.size(), cps);
    }

    public synchronized void refresh() {
        connectionsPoints = null;
        connectionsPoints = new ConsistentHash<Connection>(Hashing.murmur3_128(), cps.size(), cps);
    }

    public void addConnectionPoint(Connection cp) {
        this.cps.add(cp);
        refresh();
    }

    public void connect() {
        for (Connection cp : connectionsPoints.getCircle().values()) {
            cp.start();
        }
    }

    public Set<Connection> broadcast(Object obj) {
        HashSet<Connection> set = new HashSet<Connection>();
        for (Connection cp : connectionsPoints.getCircle().values()) {
            if (cp.send(obj)) {
                set.add(cp);
            }
        }
        return set;
    }

    public boolean send(Object key, Object obj) {
        Connection cp = this.connectionsPoints.get(key);
        if (cp == null) {
            return false;
        }
        return cp.send(obj);
    }

    public boolean send(Object obj) {
        count++;
        int index = count % cps.size();
        Connection cp = cps.get(index);
        if (cp == null) {
            return false;
        }
        return cp.send(obj);
    }

}
