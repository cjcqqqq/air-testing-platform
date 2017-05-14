package com.university.shenyang.air.testing.gateway.procotol.device.receiver;

import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.procotol.Protocols;
import com.university.shenyang.air.testing.gateway.procotol.device.DeviceCommand;
import com.university.shenyang.air.testing.gateway.util.Constants;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * 终端校时
 * Created by chenjc on 2017/05/03.
 */
@Protocols(id = "08", type = Constants.PROTOCOL_GB)
public class GB_08_Timing extends DeviceCommand {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(GB_08_Timing.class);

    @Override
    public void processor(ChannelHandlerContext ctx, Packet packet) {
        try {
            // 获取登入链路上下文
            ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(packet.getUniqueMark());

            // 判断设备是否合法并登入
            if (loginCtx == ctx) {
                // 设备消息通用应答
                packet = super.terminalMessageCommonAnswer(packet);
                // 发送应答
                ctx.writeAndFlush(packet);
            } else {
                logger.info("该设备信息不存在或未进行登入，设备标识码为：{}" + packet.getUniqueMark());
                ctx.close();
            }
        } catch (Exception ex) {
            logger.error("解析终端校时信息出错:" + ex);
        }
    }
}
