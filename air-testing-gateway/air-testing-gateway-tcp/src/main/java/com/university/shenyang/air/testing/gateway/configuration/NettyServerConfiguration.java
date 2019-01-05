package com.university.shenyang.air.testing.gateway.configuration;

import com.university.shenyang.air.testing.gateway.tcp.server.TcpServer;
import com.university.shenyang.air.testing.gateway.tcp.server.initializer.DeviceChannelInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chenjc on 2017/05/07.
 */
@Configuration
public class NettyServerConfiguration {
    @Value("${air.testing.platform.server.address:0.0.0.0}")
    private String terminalTcpHost;

    @Value("${air.testing.platform.server.port:20400}")
    private int terminalTcpPort;

    @Value("${netty.option.so_keepalive:true}")
    private boolean soKeepalive;

    @Value("${netty.option.so_sndbuf:262144}")
    private int soSndbuf;

    @Value("${netty.option.so_rcvbuf:131072}")
    private int soRcvbuf;

    @Value("${netty.option.so_backlog:200}")
    private int soBacklog;

    @Value("${netty.option.tcp_nodelay:true}")
    private boolean tcpNodelay;

    @Value("${netty.child.option.so_keepalive:true}")
    private boolean childSoKeepalive;

    @Value("${netty.child.option.so_sndbuf:262144}")
    private int childSoSndbuf;

    @Value("${netty.child.option.so_rcvbuf:131072}")
    private int childSoRcvbuf;

    @Value("${netty.child.option.so_backlog:200}")
    private int childSoBacklog;

    @Value("${netty.child.option.tcp_nodelay:true}")
    private boolean childTcpNodelay;

    /**
     * Server for Terminal Interface Protocol
     *
     * @return
     */
    @Bean
    public TcpServer terminalServer(DeviceChannelInitializer channelInitializer) {
        TcpServer tcpServer = new TcpServer(terminalTcpHost,
                terminalTcpPort,
                soKeepalive,
                soSndbuf,
                soRcvbuf,
                soBacklog,
                tcpNodelay,
                childSoKeepalive,
                childSoSndbuf,
                childSoRcvbuf,
                childSoBacklog,
                childTcpNodelay,
                channelInitializer);
        return tcpServer;
    }

}
