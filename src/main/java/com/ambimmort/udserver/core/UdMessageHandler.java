/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.core;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class UdMessageHandler extends IoHandlerAdapter {

    UdMessageCache cache = UdMessageCache.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String ip = "";

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
        this.ip = addr.getAddress().getHostAddress();
        System.out.println("dpi session opend[" + sdf.format(new Date(session.getCreationTime())) + "].  remote address: " + session.getRemoteAddress());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("dpi session closed[" + sdf.format(new Date(session.getCreationTime())) + "].  remote address: " + session.getRemoteAddress()+"\t lastIoTime:["+ sdf.format(new Date(session.getLastIoTime()))+"]");
        
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
//        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        UdRawMessage msg = (UdRawMessage) message;
        msg.setIp(ip);
        cache.add(msg);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        session.close(true);
    }

}
