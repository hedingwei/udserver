package com.ambimmort.udserver.core;


import com.ambimmort.udserver.configuration.UdServerConfig;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * Hello world!
 *
 */
public class UdServer {

    private IoAcceptor acceptor = null;

    private UdServerConfig config = null;

    public UdServer() {
        config = UdServerConfig.getConfig();
        acceptor = new NioSocketAcceptor();
        if (config.getLog().isLogcodec()) {
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        }
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new UdProtocolCodecFactory()));
        acceptor.setHandler(new UdMessageHandler());
        acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, config.getServer().getTimeout());
    }

    public void bind() {
        try {
            acceptor.bind(new InetSocketAddress(config.getServer().getPort()));
        } catch (IOException ex) {
            Logger.getLogger(UdServer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("%%%%%%%%%%");
        }
    }

    public void unbind() {
        acceptor.unbind();
    }

    public static void main(String[] args) throws IOException {

        
        UdServer server = new UdServer();
        server.bind();
        
        UdMessageProcessor.init();

//        new TableInitializer().init();
//        System.out.println("Table Initialized");
//        UdWindow.getInstance();
//        System.out.println();

    }
}
