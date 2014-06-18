/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.monitor;

import com.ambimmort.udserver.core.UdServer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;

/**
 *
 * @author 定巍
 */
public class RuntimeMonitor {

    private static RuntimeMonitor instance = null;

    private UdServer server = null;

    private Server httpServer = null;

    private RuntimeMonitor() {
        httpServer = new Server(8080);
        try {
            ServletHandler handler = new ServletHandler();
            httpServer.setHandler(handler);

        // Passing in the class for the servlet allows jetty to instantite an instance of that servlet and mount it
            // on a given context path.
            // !! This is a raw Servlet, not a servlet that has been configured through a web.xml or anything like that !!
            handler.addServletWithMapping(ProfilerServlet.class, "/profiler");
            httpServer.start();
//            httpServer.join();
        } catch (Exception ex) {
            Logger.getLogger(RuntimeMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setServer(UdServer server) {
        this.server = server;
    }

    public UdServer getServer() {
        return server;
    }
    
    

    public static RuntimeMonitor getInstance() {
        if (instance == null) {
            instance = new RuntimeMonitor();
        }
        return instance;
    }
}
