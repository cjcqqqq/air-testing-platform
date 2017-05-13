package com.university.shenyang.air.testing.gateway;

import com.university.shenyang.air.testing.gateway.configuration.NettyServerStartup;
import com.university.shenyang.air.testing.gateway.tcp.server.TcpServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenjc
 * @version 1.0
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.university.shenyang.air.testing,com.university.shenyang.air.testing.gateway")
public class Application implements ApplicationContextAware {
    private static ApplicationContext appContext;

    @Autowired
    TcpServer tcpServer;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.setWebEnvironment(true);
        application.run(args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Application.appContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return Application.appContext;
    }
//
//    public void terminalServer(TerminalChannelInitializer terminalChannelInitializer) {
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup);
//            b.channel(NioServerSocketChannel.class);
//            b.childHandler(new TerminalChannelInitializer(dispatchTcpHandler, outboundHandler, heartbeat));
//
//            // 服务器绑定端口监听
//            ChannelFuture f = b.bind(serverPort).sync();
//            // 监听服务器关闭监听
//            f.channel().closeFuture().sync();
//
//            // 可以简写为
//			/* b.bind(portNumber).sync().channel().closeFuture().sync(); */
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }
}
