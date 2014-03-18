/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.core;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class UdMessageHandler extends IoHandlerAdapter {

    UdMessageCache cache = UdMessageCache.getInstance();

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
//        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        UdRawMessage msg = (UdRawMessage) message;
        cache.add(msg);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        session.close(true);
    }

}
