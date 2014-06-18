/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class ProfilerServlet extends HttpServlet {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        JSONObject rt = new JSONObject();
        JSONArray rst = new JSONArray();
        for (IoEventType iet : RuntimeMonitor.getInstance().getServer().getProfiler().getEventsToProfile()) {
            JSONObject obj = new JSONObject();
            obj.put("EventType", iet.name());
            try {
                obj.put("AverageTime", RuntimeMonitor.getInstance().getServer().getProfiler().getAverageTime(iet));
            } catch (Exception e) {
                obj.put("AverageTime", 0);
            }
            obj.put("MaximumTime", RuntimeMonitor.getInstance().getServer().getProfiler().getMaximumTime(iet));
            obj.put("MinimumTime", RuntimeMonitor.getInstance().getServer().getProfiler().getMinimumTime(iet));
            obj.put("TotalTime", RuntimeMonitor.getInstance().getServer().getProfiler().getTotalTime(iet));
            obj.put("TotalCalls", RuntimeMonitor.getInstance().getServer().getProfiler().getTotalCalls(iet));
            rst.add(obj);

        }
        rt.put("events", rst);
        JSONObject tt = new JSONObject();
        IoAcceptor acceptor = RuntimeMonitor.getInstance().getServer().getAcceptor();
        tt.put("LastActivationTime", sdf.format(new Date(RuntimeMonitor.getInstance().getServer().getAcceptor().getActivationTime())));
        tt.put("ScheduledWriteBytes", acceptor.getScheduledWriteBytes());
        tt.put("ScheduledWriteMessages", acceptor.getScheduledWriteMessages());
        tt.put("statistic", JSONObject.fromObject(acceptor.getStatistics()));
        tt.put("sessionCount", acceptor.getManagedSessionCount());
        JSONObject sessions = new JSONObject();
        for(IoSession s:acceptor.getManagedSessions().values()){
            JSONObject session = new JSONObject();
            session.put("ScheduledWriteBytes", s.getScheduledWriteBytes());
            session.put("ScheduledWriteMessages", s.getScheduledWriteMessages());
            session.put("LastIoTime",sdf.format(new Date(s.getLastIoTime())));
            session.put("LastReadTime",sdf.format(new Date(s.getLastReadTime())));
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
            sessions.put(""+s.getId(), session);
        }
        tt.put("sessions", sessions);
        rt.put("commons", tt);
        PrintWriter pw = resp.getWriter();
        pw.print(rt.toString());
        pw.close();
    }

}
