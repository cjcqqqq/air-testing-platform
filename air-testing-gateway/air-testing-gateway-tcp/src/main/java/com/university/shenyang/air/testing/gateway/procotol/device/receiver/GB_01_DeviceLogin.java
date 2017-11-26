package com.university.shenyang.air.testing.gateway.procotol.device.receiver;

import com.university.shenyang.air.testing.gateway.common.kit.Convert;
import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.gateway.procotol.Protocols;
import com.university.shenyang.air.testing.gateway.procotol.device.DeviceCommand;
import com.university.shenyang.air.testing.gateway.util.Constants;
import com.university.shenyang.air.testing.model.DeviceInfo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * Created by chenjc on 2017/05/03.
 * 在注册鉴权指令中完成链路标识和设备ID的绑定关系。
 * 内部协议设备消息通过设备ID标识。
 */
@Protocols(id = "01", type = Constants.PROTOCOL_GB)
@ChannelHandler.Sharable
public class GB_01_DeviceLogin extends DeviceCommand {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(GB_01_DeviceLogin.class);

    @Override
    public void processor(ChannelHandlerContext ctx, Packet packet) {
        try {
            DeviceInfo deviceInfo = DevicesManager.getInstance().getDeviceByCode(packet.getUniqueMark());

            if (deviceInfo == null) {
                LOGGER.info("该设备不存在，设备标识码为：{}" + packet.getUniqueMark());
                // TODO
                ctx.close();
            } else {
                byte[] content = packet.getContent();
                Convert.gbDateToString(ArraysUtils.subarrays(content, 0, 6));

                // 设备消息通用应答
                packet = super.terminalMessageCommonAnswer(packet);
                // 发送应答
                ctx.writeAndFlush(packet);

                // 获取老的链路上下文
                ChannelHandlerContext oldCtx = DevicesManager.getInstance().getCtxByDeviceCode(deviceInfo.getDeviceCode());
                // 如果老的链路和当前链路不是一个链路
//                if(oldCtx != null && !ctx.channel().remoteAddress().toString().equals(oldCtx.channel().remoteAddress().toString())){
                if(oldCtx != null && oldCtx != ctx){
                    // 关闭老链路
                    oldCtx.close();
                }

                // 将设备识别码与设备远程地址与的映射存入缓存
                DevicesManager.getInstance().addDeviceCodeMappingChannel(deviceInfo.getDeviceCode(), ctx);
                // 将设备远程地址与设备识别码的映射存入缓存
                DevicesManager.getInstance().addChannelMappingDeviceCode(ctx.channel().remoteAddress().toString(), deviceInfo.getDeviceCode());
            }
        } catch (Exception ex) {
            LOGGER.error("解析登入信息出错:" + ex);
        }
    }
}
