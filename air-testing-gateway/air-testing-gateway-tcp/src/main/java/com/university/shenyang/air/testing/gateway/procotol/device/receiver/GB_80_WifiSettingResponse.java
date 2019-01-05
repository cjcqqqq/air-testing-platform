package com.university.shenyang.air.testing.gateway.procotol.device.receiver;

import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.common.kit.Convert;
import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.gateway.procotol.Protocols;
import com.university.shenyang.air.testing.gateway.procotol.device.DeviceCommand;
import com.university.shenyang.air.testing.gateway.service.CommandSendLogService;
import com.university.shenyang.air.testing.gateway.util.Constants;
import com.university.shenyang.air.testing.model.CommandSendLog;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Wifi账号密码设置应答处理
 * Created by chenjc on 2017/05/03.
 */
@Protocols(id = "80", type = Constants.PROTOCOL_GB)
@ChannelHandler.Sharable
public class GB_80_WifiSettingResponse extends DeviceCommand {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(GB_80_WifiSettingResponse.class);

    @Autowired
    CommandSendLogService commandSendLogService;

    @Override
    public void processor(ChannelHandlerContext ctx, Packet packet) {
        try {
            // 获取登入链路上下文
            ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(packet.getUniqueMark());

            // 判断设备是否合法并登入
            if (loginCtx == ctx) {
                CommandSendLog record = new CommandSendLog();
                record.setCommandId(packet.getCommandId());
                record.setDeviceCode(packet.getUniqueMark());
                record.setSendtime(Convert.strToDate(Convert.gbDateToString(ArraysUtils.subarrays(packet.getContent(), 0, 6))));
                record.setCommandStatus(packet.getAnswerId());
                commandSendLogService.updateByCodeCommandIdAndTime(record);

            } else {
                LOGGER.info("该设备信息不存在或未进行登入，设备标识码为：{}" + packet.getUniqueMark());
                ctx.close();
            }
        } catch (Exception ex) {
            LOGGER.error("解析设备Wifi设置应答信息出错:" + ex);
        }
    }
}
