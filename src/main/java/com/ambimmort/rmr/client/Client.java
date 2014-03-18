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

    private ConsistentHash<ConnectionPoint> connectionsPoints = null;

    private List<ConnectionPoint> cps = null;
    
    private int count = 0;

    public List<ConnectionPoint> getCps() {
        return cps;
    }

    public Client(List<ConnectionPoint> cps) {
        this.cps = cps;
        connectionsPoints = new ConsistentHash<ConnectionPoint>(Hashing.murmur3_128(), cps.size(), cps);
    }
    
    public Client() {
        this.cps = new ArrayList<ConnectionPoint>();
        connectionsPoints = new ConsistentHash<ConnectionPoint>(Hashing.murmur3_128(), cps.size(), cps);
    }

    public synchronized void refresh() {
        connectionsPoints = null;
        connectionsPoints = new ConsistentHash<ConnectionPoint>(Hashing.murmur3_128(), cps.size(), cps);
    }
    
    public void addConnectionPoint(ConnectionPoint cp){
        this.cps.add(cp);
        refresh();
    }

    public void connect() {
        for (ConnectionPoint cp : connectionsPoints.getCircle().values()) {
            cp.start();
        }
    }

    public Set<ConnectionPoint> broadcast(Object obj) {
        HashSet<ConnectionPoint> set = new HashSet<ConnectionPoint>();
        for (ConnectionPoint cp : connectionsPoints.getCircle().values()) {
            if (cp.send(obj)) {
                set.add(cp);
            }
        }
        return set;
    }

    public boolean send(Object key, Object obj) {
        ConnectionPoint cp = this.connectionsPoints.get(key);
        return cp.send(obj);
    }
    
    public boolean send(Object obj) {
        count++;
        int index = count%cps.size();
        ConnectionPoint cp = cps.get(index);
        return cp.send(obj);
    }

}
