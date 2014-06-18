package com.ambimmort.udserver.core;


import com.ambimmort.udserver.configuration.UdServerConfig;
import com.ambimmort.udserver.monitor.RuntimeMonitor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.statistic.ProfilerTimerFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * Hello world!
 *
 */
public class UdServer {

    private IoAcceptor acceptor = null;

    private UdServerConfig config = null;
    
    private ProfilerTimerFilter profiler = new ProfilerTimerFilter(TimeUnit.MINUTES, IoEventType.MESSAGE_RECEIVED, IoEventType.EXCEPTION_CAUGHT,IoEventType.CLOSE,IoEventType.SESSION_CLOSED,IoEventType.SESSION_CREATED,IoEventType.SESSION_OPENED,IoEventType.SESSION_IDLE);

    public UdServer() {
        config = UdServerConfig.getConfig();
        acceptor = new NioSocketAcceptor();
        
       
        acceptor.getFilterChain().addLast("profiler", profiler);
        if (config.getLog().isLogcodec()) {
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        }
        
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new UdProtocolCodecFactory()));
//        acceptor.getFilterChain().addLast("threshold", new ConnectionThrottleFilter(2000000));
        
        acceptor.setHandler(new UdMessageHandler());
        acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, config.getServer().getTimeout());
    }

    public ProfilerTimerFilter getProfiler() {
        return profiler;
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

    public IoAcceptor getAcceptor() {
        return acceptor;
    }

    public static void main(String[] args) throws IOException {

        
        UdServer server = new UdServer();
        server.bind();
        UdMessageProcessor.init();

        RuntimeMonitor.getInstance().setServer(server);
        
//        new TableInitializer().init();
//        System.out.println("Table Initialized");
//        UdWindow.getInstance();
//        System.out.println();

    }
}
