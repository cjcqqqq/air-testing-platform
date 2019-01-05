package com.university.shenyang.air.testing.collector.tcp.server.initializer;

import com.university.shenyang.air.testing.collector.tcp.server.codec.DeviceDecoder;
import com.university.shenyang.air.testing.collector.tcp.server.codec.DeviceEncoder;
import com.university.shenyang.air.testing.collector.tcp.server.handler.DeviceDispatchTcpHandler;
import com.university.shenyang.air.testing.collector.tcp.server.handler.DeviceOutboundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 初始化通道
 * Created by chenjc on 2017/05/03.
 */
@Component
public final class DeviceChannelInitializer extends ChannelInitializer<Channel> {
    @Value("${tcp.heartbeat.time:300}")
    private int heartbeat;

    private final DeviceDispatchTcpHandler dispatchTcpHandler;

    private final DeviceOutboundHandler outboundHandler;

    @Autowired
    public DeviceChannelInitializer(DeviceDispatchTcpHandler dispatchTcpHandler,
                                    DeviceOutboundHandler outboundHandler) {
        this.dispatchTcpHandler = dispatchTcpHandler;
        this.outboundHandler = outboundHandler;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
//        pipeline.addLast("logging",new LoggingHandler(LogLevel.INFO));
        //配置服务端监听读超时，即无法收到客户端发的心跳信息的最长时间间隔：默认5分钟
        pipeline.addLast("ping", new IdleStateHandler(heartbeat, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast("DeviceDecoder", new DeviceDecoder());
        pipeline.addLast("DeviceEncoder", new DeviceEncoder());

        pipeline.addLast(outboundHandler);
        pipeline.addLast("handle", dispatchTcpHandler);
    }
}
