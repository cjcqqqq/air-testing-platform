package com.university.shenyang.air.testing.collector.procotol;

import io.netty.channel.ChannelHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载所有指令协议处理类
 * Created by chenjc on 2017/05/03.
 */
@Component
@ChannelHandler.Sharable
public class ProtocolDispatcher {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(ProtocolDispatcher.class);
    private final Map<String, Command> handlers;

    @Autowired
    public ProtocolDispatcher(List<Command> handlerList) {
        Map<String, Command> map = new HashMap<>();
        for (Command handler : handlerList) {
            Class<? extends Command> clazz =handler.getClass();
            if (!clazz.isAnnotationPresent(Protocols.class)) {
                continue;
            }
            Protocols protocols = clazz.getAnnotation(Protocols.class);
            String key= protocols.id();
            if(!StringUtils.isEmpty(protocols.type())){
                key= protocols.type()+"_"+ protocols.id();
            }
            LOGGER.info("{} loading handler : {}", key, handler.getClass());
            map.put(key, handler);
        }
        LOGGER.info("load proto count:{}",map.size());
        handlers = Collections.unmodifiableMap(map);
    }

    public Command getHandler(String cmdId) {
        return handlers.get(cmdId);
    }

}
