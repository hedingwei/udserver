/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.rmr.client;

import com.ambimmort.rmr.plugins.fstserialization.FstSerializationCodecFactory;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 *
 * @author 定巍
 */
public class Connection {

    private Client client = null;

    private IoConnector connector = null;

    private Thread thread = null;

    private IoSession session = null;

    private EndPoint endPoint = null;

    private boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    public IoConnector getConnector() {
        return connector;
    }

    public IoSession getSession() {
        return session;
    }

    public Connection(String host, int port, Client client) {
        this.client = client;
        client.addConnectionPoint(this);
        this.endPoint = new EndPoint();
        this.endPoint.setHost(host);
        this.endPoint.setPort(port);
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(60 * 60 * 1000);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new FstSerializationCodecFactory()));
        connector.setHandler(new IoHandlerAdapter() {
            @Override
            public void sessionOpened(IoSession session) throws Exception {
                Connection.this.session = session;
                System.out.println("session [" + session.getId() + "]["+session.isConnected()+"] opened local:" + session.getLocalAddress() + " remote:" + session.getRemoteAddress());
                System.out.println("**********----***********");
                Connection.this.connected = true;
                if (!Connection.this.client.getCps().contains(Connection.this)) {
                    Connection.this.client.addConnectionPoint(Connection.this);
                    Connection.this.client.refresh();
                }
            }

            @Override
            public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
                Connection.this.connected = false;
                cause.printStackTrace();
                session.close(true);

                reconnect();
            }

            @Override
            public void sessionClosed(IoSession session) throws Exception {
                System.out.println("session closed. remote address: " + session.getRemoteAddress());
                Connection.this.connected = false;
                Connection.this.client.getCps().remove(Connection.this);
                Connection.this.client.refresh();
                reconnect();
            }

            private void reconnectOnStart() throws RuntimeIoException {
                try {
                    Connection.this.connected = false;
                    Thread.sleep(1000);

                } catch (InterruptedException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }
                session = null;
                connector.connect(new InetSocketAddress(Connection.this.endPoint.getHost(), Connection.this.endPoint.getPort()));
            }

            private void reconnect() {
                while (true) {
                    try {
                        reconnectOnStart();
                        break;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void start() {
        if ((this.thread == null)) {
            this.thread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            reconnectOnStart();
                            break;
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }

                private void reconnectOnStart() throws RuntimeIoException {
                    try {
                        Connection.this.connected = false;
                        Thread.sleep(1000);
                        System.out.println("reconnecting");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    session = null;

                    connector.connect(new InetSocketAddress(Connection.this.endPoint.getHost(), Connection.this.endPoint.getPort()));
                }
            });
            this.thread.start();
        } else {

        }

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
