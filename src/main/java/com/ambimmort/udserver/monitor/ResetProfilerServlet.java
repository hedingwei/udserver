/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.monitor;

import com.ambimmort.rmr.client.Client;
import com.ambimmort.rmr.client.Connection;
import com.ambimmort.rmr.client.ConsistentHash;
import com.ambimmort.udserver.core.UdMessageProcessor;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class ResetProfilerServlet extends HttpServlet {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RuntimeMonitor.getInstance().getServer().getProfiler().setEventsToProfile(IoEventType.MESSAGE_RECEIVED, IoEventType.EXCEPTION_CAUGHT,IoEventType.CLOSE,IoEventType.SESSION_CLOSED,IoEventType.SESSION_CREATED,IoEventType.SESSION_OPENED,IoEventType.SESSION_IDLE);
        
    }

}
