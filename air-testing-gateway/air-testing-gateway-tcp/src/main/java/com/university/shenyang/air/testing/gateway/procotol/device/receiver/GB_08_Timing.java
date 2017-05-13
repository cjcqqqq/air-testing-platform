package com.university.shenyang.air.testing.gateway.procotol.device.receiver;

import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.procotol.Protocols;
import com.university.shenyang.air.testing.gateway.procotol.device.DeviceCommand;
import com.university.shenyang.air.testing.gateway.util.Constants;
import io.netty.channel.ChannelHandlerContext;

/**
 * 终端校时
 * Created by chenjc on 2017/05/03.
 */
@Protocols(id = "08", type = Constants.PROTOCOL_GB)
public class GB_08_Timing extends DeviceCommand {
    @Override
    public void processor(ChannelHandlerContext ctx, Packet packet) {
        // 设备消息通用应答
        packet = super.terminalMessageCommonAnswer(packet);
        // 发送应答
        ctx.writeAndFlush(packet);
    }
}
