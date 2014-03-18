/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ho.yaml.Yaml;

/**
 *
 * @author 定巍
 */
public class UdServerConfig {

    private Server server;
    private Cache cache;
    private Log log;
    private HashMap router;

    public HashMap getRouter() {
        return router;
    }

    public void setRouter(HashMap router) {
        this.router = router;
    }

    
    
    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }


    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public static UdServerConfig getConfig() {
        try {
            if (new File("etc/udserver.yml").exists()) {
                return Yaml.loadType(new File("etc/udserver.yml"), UdServerConfig.class);
            } else if (new File("src/main/resources/udserver.yml").exists()) {
                return Yaml.loadType(new File("src/main/resources/udserver.yml"), UdServerConfig.class);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UdServerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new UdServerConfig();
    }
}
