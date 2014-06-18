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
import static com.ambimmort.udserver.monitor.ServerProfilerServlet.sdf;
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
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class ClientProfilerServlet extends HttpServlet {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        JSONArray circle = new JSONArray();
        for (String k : UdMessageProcessor.getClients().keySet()) {
            Client c = UdMessageProcessor.getClients().get(k);
            ConsistentHash<Connection> hash = c.getConnectionsPoints();
            JSONObject hashObj = new JSONObject();
            for (Long l : hash.getCircle().keySet()) {
                Connection con = hash.getCircle().get(l);
                IoConnector cc = con.getConnector();
                JSONObject tt = new JSONObject();
                tt.put("LastActivationTime", sdf.format(new Date(RuntimeMonitor.getInstance().getServer().getAcceptor().getActivationTime())));
                tt.put("ScheduledWriteBytes", cc.getScheduledWriteBytes());
                tt.put("ScheduledWriteMessages", cc.getScheduledWriteMessages());
                tt.put("statistic", JSONObject.fromObject(cc.getStatistics()));
                tt.put("sessionCount", cc.getManagedSessionCount());
                IoSession s = con.getSession();
                JSONObject session = new JSONObject();
                if (s == null) {

                } else {
                    session.put("ScheduledWriteBytes", s.getScheduledWriteBytes());
                    session.put("ScheduledWriteMessages", s.getScheduledWriteMessages());
                    session.put("LastIoTime", sdf.format(new Date(s.getLastIoTime())));
                    session.put("LastReadTime", sdf.format(new Date(s.getLastReadTime())));
                    session.put("LocalAddress", s.getLocalAddress().toString());
                    session.put("RemoteAddress", s.getRemoteAddress().toString());
                    session.put("isBothIdle", s.isBothIdle());
                    session.put("isClosing", s.isClosing());
                    session.put("isConnected", s.isConnected());
                    session.put("isReadSuspended", s.isReadSuspended());
                    session.put("isReaderIdle", s.isReaderIdle());
                    session.put("isWriteSuspended", s.isWriteSuspended());
                    session.put("isWriterIdle", s.isWriterIdle());
                    session.put("id", s.getId());
                }

                hashObj.put("connector", tt);
                hashObj.put("session", session);
            }
            circle.add(hashObj);
        }
        JSONObject clientObj = new JSONObject();
        clientObj.put("circle", circle);
        PrintWriter pw = resp.getWriter();
        pw.print(clientObj.toString());
        pw.close();
    }

}
