/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.configuration;

/**
 *
 * @author 定巍
 */
public class Cache {

    private int limit;
    private int period;
    private int threads_max;
    private int check_interval;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getThreads_max() {
        return threads_max;
    }

    public void setThreads_max(int threads_max) {
        this.threads_max = threads_max;
    }

    public int getCheck_interval() {
        return check_interval;
    }

    public void setCheck_interval(int check_interval) {
        this.check_interval = check_interval;
    }

}
