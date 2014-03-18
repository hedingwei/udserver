/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.rmr.client;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 *
 * @author 定巍
 */
public class ConnectionPoint {

    private Client client = null;

    private IoConnector connector = null;

    private Thread thread = null;

    private IoSession session = null;

    private EndPoint endPoint = null;

    public ConnectionPoint(String host, int port, Client client) {
        this.client = client;
        client.addConnectionPoint(this);
        this.endPoint = new EndPoint();
        this.endPoint.setHost(host);
        this.endPoint.setPort(port);
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(10 * 1000);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        connector.setHandler(new IoHandlerAdapter() {
            @Override
            public void sessionOpened(IoSession session) throws Exception {
                System.out.println("session opend");
            }

            @Override
            public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
                reconnect();
            }

            @Override
            public void sessionClosed(IoSession session) throws Exception {

                ConnectionPoint.this.client.getCps().remove(ConnectionPoint.this);
                ConnectionPoint.this.client.refresh();

                reconnect();
            }

            private void reconnectOnStart() throws RuntimeIoException {
                try {
                    Thread.sleep(1000);
                    System.out.println("reconnecting");
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConnectionPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
                session = null;
                ConnectFuture future = connector.connect(new InetSocketAddress(ConnectionPoint.this.endPoint.getHost(), ConnectionPoint.this.endPoint.getPort()));
                future.awaitUninterruptibly();
                session = future.getSession();
                session.getCloseFuture().awaitUninterruptibly();
            }

            private void reconnect() {
                while (true) {
                    try {
                        reconnectOnStart();
                        break;
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    public void start() {
        this.thread = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        reconnectOnStart();
                        break;
                    } catch (Exception e) {

                    }

                }
            }

            private void reconnectOnStart() throws RuntimeIoException {
                try {
                    Thread.sleep(1000);
                    System.out.println("reconnecting");
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConnectionPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
                session = null;
                ConnectFuture future = connector.connect(new InetSocketAddress(ConnectionPoint.this.endPoint.getHost(), ConnectionPoint.this.endPoint.getPort()));
                future.awaitUninterruptibly();
                session = future.getSession();
                session.getCloseFuture().awaitUninterruptibly();
            }
        });
        this.thread.start();
    }

    public boolean send(Object obj) {
        if (session != null) {
            if (session.isConnected()) {
                session.write(obj);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "ConnectionPoint{" + "endPoint=" + endPoint + "," + session + '}';
    }

}
