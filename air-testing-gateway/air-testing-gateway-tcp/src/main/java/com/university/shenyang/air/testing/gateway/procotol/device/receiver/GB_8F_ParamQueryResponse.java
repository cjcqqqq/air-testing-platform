package com.university.shenyang.air.testing.gateway.procotol.device.receiver;

import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.common.kit.Convert;
import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.gateway.pojo.DeviceParam;
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
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 设备参数查询应答处理
 * Created by chenjc on 2017/05/03.
 */
@Protocols(id = "8F", type = Constants.PROTOCOL_GB)
@ChannelHandler.Sharable
public class GB_8F_ParamQueryResponse extends DeviceCommand {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(GB_8F_ParamQueryResponse.class);

    @Autowired
    CommandSendLogService commandSendLogService;

    @Autowired
    public RedisTemplate<String, DeviceParam> redisTemplate;

    @Override
    public void processor(ChannelHandlerContext ctx, Packet packet) {
        try {
            // 获取登入链路上下文
            ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(packet.getUniqueMark());

            // 判断设备是否合法并登入
            if (loginCtx == ctx) {
                // queryTime; 查询时间 6个字节
                String queryTime = Convert.gbDateToString(ArraysUtils.subarrays(packet.getContent(), 0, 6));

                if (packet.getAnswerId() == 1) {
                    DeviceParam deviceParam = new DeviceParam();
                    deviceParam.setWifiAccount(new String(ArraysUtils.subarrays(packet.getContent(), 6, 20), "ASCII").trim());
                    deviceParam.setWifiPassword(new String(ArraysUtils.subarrays(packet.getContent(), 26, 20), "ASCII").trim());
                    int interval = Convert.byte2Int(ArraysUtils.subarrays(packet.getContent(), 46, 2), 2);
                    deviceParam.setInterval(interval);

                    redisTemplate.opsForValue().set(Constants.PARAM_REDIS_KEY_PREFIX + packet.getUniqueMark() + "_" + Convert.bytesToDateString(ArraysUtils.subarrays(packet.getContent(), 0, 6)), deviceParam, 5, TimeUnit.MINUTES);
                }

                CommandSendLog record = new CommandSendLog();
                record.setCommandId(packet.getCommandId());
                record.setDeviceCode(packet.getUniqueMark());
                record.setSendtime(Convert.strToDate(queryTime));
                record.setCommandStatus(packet.getAnswerId());
                commandSendLogService.updateByCodeCommandIdAndTime(record);
            } else {
                LOGGER.info("该设备信息不存在或未进行登入，设备标识码为：{}" + packet.getUniqueMark());
                ctx.close();
            }
        } catch (Exception ex) {
            LOGGER.error("解析设备参数查询应答信息出错:" + ex);
        }
    }
}
