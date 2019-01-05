package com.university.shenyang.air.testing.gateway.procotol;

import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import io.netty.channel.ChannelHandlerContext;

/**
 * 协议解析基类
 * Created by chenjc on 2017/05/03.
 */
public abstract class Command {

    public abstract void processor(ChannelHandlerContext ctx,Packet packet);

}
