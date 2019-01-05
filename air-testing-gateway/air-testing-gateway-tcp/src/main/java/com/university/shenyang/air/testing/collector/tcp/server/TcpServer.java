package com.university.shenyang.air.testing.collector.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.Lifecycle;

/**
 * Created by chenjc on 2017/05/03.
 */
public class TcpServer implements Lifecycle, BeanNameAware {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(TcpServer.class);

    private final ServerBootstrap serverBootstrap;
    private final NioEventLoopGroup group;
    private volatile boolean started;
    private ChannelFuture channel;
    private String beanName;
    private final String host;
    private final int port;
    private boolean soKeepalive = true;
    private int soSndbuf = 262144;
    private int soRcvbuf = 131072;
    private int soBacklog = 200;
    private boolean tcpNodelay = true;
    private boolean childSoKeepalive = true;
    private int childSoSndbuf = 262144;
    private int childSoRcvbuf = 131072;
    private int childSoBacklog = 200;
    private boolean childTcpNodelay = true;

    public TcpServer(String host,
                     int port,
                     ChannelHandler channelHandler) {
        this.host = host;
        this.port = port;
        this.serverBootstrap = new ServerBootstrap();
        this.group = new NioEventLoopGroup();
        this.serverBootstrap.channel(NioServerSocketChannel.class);
        this.serverBootstrap.group(this.group);

        serverBootstrap.option(ChannelOption.SO_KEEPALIVE, soKeepalive);
        serverBootstrap.option(ChannelOption.SO_SNDBUF, soSndbuf);
        serverBootstrap.option(ChannelOption.SO_RCVBUF, soRcvbuf);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, soBacklog);
        serverBootstrap.option(ChannelOption.TCP_NODELAY, tcpNodelay);

        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, childSoKeepalive);
        serverBootstrap.childOption(ChannelOption.SO_SNDBUF, childSoSndbuf);
        serverBootstrap.childOption(ChannelOption.SO_RCVBUF, childSoRcvbuf);
        serverBootstrap.childOption(ChannelOption.SO_BACKLOG, childSoBacklog);
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, childTcpNodelay);

        this.serverBootstrap.childHandler(channelHandler);
    }

    public TcpServer(String host,
                     int port,
                     boolean soKeepalive,
                     int soSndbuf,
                     int soRcvbuf,
                     int soBacklog,
                     boolean tcpNodelay,
                     boolean childSoKeepalive,
                     int childSoSndbuf,
                     int childSoRcvbuf,
                     int childSoBacklog,
                     boolean childTcpNodelay,
                     ChannelHandler channelHandler) {
        this.host = host;
        this.port = port;
        this.serverBootstrap = new ServerBootstrap();
        this.group = new NioEventLoopGroup();
        this.serverBootstrap.channel(NioServerSocketChannel.class);
        this.serverBootstrap.group(this.group);

        serverBootstrap.option(ChannelOption.SO_KEEPALIVE, soKeepalive);
        serverBootstrap.option(ChannelOption.SO_SNDBUF, soSndbuf);
        serverBootstrap.option(ChannelOption.SO_RCVBUF, soRcvbuf);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, soBacklog);
        serverBootstrap.option(ChannelOption.TCP_NODELAY, tcpNodelay);

        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, childSoKeepalive);
        serverBootstrap.childOption(ChannelOption.SO_SNDBUF, childSoSndbuf);
        serverBootstrap.childOption(ChannelOption.SO_RCVBUF, childSoRcvbuf);
        serverBootstrap.childOption(ChannelOption.SO_BACKLOG, childSoBacklog);
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, childTcpNodelay);

        this.serverBootstrap.childHandler(channelHandler);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void start() {
        try {
            InetAddress e = InetAddress.getByName(this.host);
            this.channel = this.serverBootstrap.bind(new InetSocketAddress(e, this.port)).sync();
        } catch (Exception var2) {
            LOGGER.error("Unable to start TCP server \'" + this.beanName + "\'", var2);
            this.started = false;
            return;
        }

        LOGGER.info("Server \'{}\' started on {}:{}", new Object[]{this.beanName, this.host, Integer.valueOf(this.port)});
        this.started = true;
    }

    @Override
    public void stop() {
        if (this.started) {
            try {
                this.channel.channel().close();
                return;
            } catch (Exception var5) {
                LOGGER.error("Unable to stop server \'" + this.beanName + "\'", var5);
            } finally {
                this.group.shutdownGracefully(0L, 0L, TimeUnit.MILLISECONDS);
            }

        }
    }

    @Override
    public boolean isRunning() {
        return this.started;
    }
}
