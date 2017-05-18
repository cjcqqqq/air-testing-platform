package com.university.shenyang.air.testing.gateway.procotol.device.receiver;

import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.procotol.Protocols;
import com.university.shenyang.air.testing.gateway.procotol.device.DeviceCommand;
import com.university.shenyang.air.testing.gateway.util.Constants;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * 终端校时
 * Created by chenjc on 2017/05/03.
 */
@Protocols(id = "81", type = Constants.PROTOCOL_GB)
@ChannelHandler.Sharable
public class GB_81_CollectIntervalSettingResponse extends DeviceCommand {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(GB_81_CollectIntervalSettingResponse.class);

    @Override
    public void processor(ChannelHandlerContext ctx, Packet packet) {
        try {
            // 获取登入链路上下文
            ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(packet.getUniqueMark());

            // 判断设备是否合法并登入
            if (loginCtx == ctx) {
                // TODO

            } else {
                logger.info("该设备信息不存在或未进行登入，设备标识码为：{}" + packet.getUniqueMark());
                ctx.close();
            }
        } catch (Exception ex) {
            logger.error("解析设备采集间隔设置应答信息出错:" + ex);
        }
    }
}
