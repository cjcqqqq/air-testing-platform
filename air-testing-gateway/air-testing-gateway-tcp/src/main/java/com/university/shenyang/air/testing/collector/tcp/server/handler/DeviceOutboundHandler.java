package com.university.shenyang.air.testing.collector.tcp.server.handler;

import com.university.shenyang.air.testing.collector.procotol.ProtocolDispatcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 下发指令handler
 * Created by chenjc on 2017/05/03.
 */
@ChannelHandler.Sharable
@Component
public class DeviceOutboundHandler extends ChannelOutboundHandlerAdapter {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(DeviceOutboundHandler.class);
    @Autowired
    private ProtocolDispatcher protocolDispatcher;


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if ((msg instanceof Object)) {

        }
        super.write(ctx, msg, promise);
    }
}
