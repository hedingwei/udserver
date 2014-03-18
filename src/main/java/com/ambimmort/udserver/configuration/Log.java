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
public class Log {

    private boolean logcodec;
    private String logconfigfile;

    public boolean isLogcodec() {
        return logcodec;
    }

    public void setLogcodec(boolean logcodec) {
        this.logcodec = logcodec;
    }

    public String getLogconfigfile() {
        return logconfigfile;
    }

    public void setLogconfigfile(String logconfigfile) {
        this.logconfigfile = logconfigfile;
    }

}
