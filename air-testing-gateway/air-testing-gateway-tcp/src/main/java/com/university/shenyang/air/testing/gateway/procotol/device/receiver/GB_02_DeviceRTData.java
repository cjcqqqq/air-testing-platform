package com.university.shenyang.air.testing.gateway.procotol.device.receiver;

import com.university.shenyang.air.testing.gateway.common.kit.Convert;
import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.procotol.Protocols;
import com.university.shenyang.air.testing.gateway.procotol.device.DeviceCommand;
import com.university.shenyang.air.testing.gateway.util.Constants;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * 解析02实时信息上报数据
 * Created by chenjc on 2017/05/03.
 */
@Protocols(id = "02", type = Constants.PROTOCOL_GB)
@ChannelHandler.Sharable
public class GB_02_DeviceRTData extends DeviceCommand {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(GB_02_DeviceRTData.class);

    @Override
    public void processor(ChannelHandlerContext ctx, Packet packet) {
        try {
            // 获取登入链路上下文
            ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(packet.getUniqueMark());

            // 判断设备是否合法并登入
            if(loginCtx == ctx){

                byte[] content = ArraysUtils.subarrays(packet.getContent(), 0);//复制一份数组，下边内容进行解析时会进行截断。

                // 设备消息通用应答
                packet = super.terminalMessageCommonAnswer(packet);
                // 发送应答
                ctx.writeAndFlush(packet);

                // code
                String code = packet.getUniqueMark();
                // collectTime; // 采集时间 6个字节
                String collectTime = Convert.gbDateToString(ArraysUtils.subarrays(content, 0, 6));
                // sim; //sim卡号 20位 4个字节
                String sim = new String(ArraysUtils.subarrays(content, 6, 20), "ASCII");
//                long sim = Convert.byte2Long(ArraysUtils.subarrays(content, 6, 4), 4);
                // pm1_0; // pm1.0 单位:微克/立方米 整数 4个字节
                int pm1_0 = Convert.byte2Int(ArraysUtils.subarrays(content, 26, 4), 4);
                // pm2_5; // pm2.5 单位:微克/立方米 整数 4个字节
                int pm2_5 = Convert.byte2Int(ArraysUtils.subarrays(content, 30, 4), 4);
                // pm10;      // pm10  单位:微克/立方米 整数 4个字节
                int pm10 = Convert.byte2Int(ArraysUtils.subarrays(content, 34, 4), 4);
                // formaldehyde; // 甲醛 0.00-9.99 * 100 2个字节
                int formaldehyde = Convert.byte2Int(ArraysUtils.subarrays(content, 38, 2), 2);
                // temperature; // 温度 0-255 * 10 偏移500 0表示-50摄氏度 2个字节
                int temperature = Convert.byte2Int(ArraysUtils.subarrays(content, 40, 2), 2);
                // humidity; // 湿度 0-100 1个字节
                int humidity = Convert.byte2Int(ArraysUtils.subarrays(content, 42, 1), 1);
                // co; // 一氧化碳 预留4个字节
                int co = Convert.byte2Int(ArraysUtils.subarrays(content, 43, 4), 4);
                // co2; // 二氧化碳 预留4个字节
                int co2 = Convert.byte2Int(ArraysUtils.subarrays(content, 47, 4), 4);
                // no; // 一氧化氮 预留4个字节
                int no = Convert.byte2Int(ArraysUtils.subarrays(content, 51, 4), 4);
                // no2; // 二氧化氮 预留4个字节
                int no2 = Convert.byte2Int(ArraysUtils.subarrays(content, 55, 4), 4);
                // o3; // 臭氧 预留4个字节
                int o3 = Convert.byte2Int(ArraysUtils.subarrays(content, 59, 4), 4);
                // so2; // 二氧化硫 预留4个字节
                int so2 = Convert.byte2Int(ArraysUtils.subarrays(content, 63, 4), 4);
                // tvoc; // 有机气态物质 预留4个字节
                int tvoc = Convert.byte2Int(ArraysUtils.subarrays(content, 67, 4), 4);
                // windSpeed; // 风速 预留4个字节
                int windSpeed = Convert.byte2Int(ArraysUtils.subarrays(content, 71, 4), 4);
                // windDirection; // 风向 预留2个字节
                int windDirection = Convert.byte2Int(ArraysUtils.subarrays(content, 75, 2), 2);
                // longitude; // 经度 以度为单位的经度值乘以10的6次方，精确到百万分之一度。 4个字节
                long longitude = Convert.byte2Long(ArraysUtils.subarrays(content, 77, 4), 4);
                // latitude; // 纬度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度。 4个字节
                long latitude = Convert.byte2Long(ArraysUtils.subarrays(content, 81, 4), 4);
                // electricity; // 太阳能电源电量0-100 1个字节
                int electricity = Convert.byte2Int(ArraysUtils.subarrays(content, 85, 1), 1);

                logger.debug("收到设备实时采集数据 设备识别码{}，" +
                        "采集时间：{}，" +
                        "sim卡号：{}，" +
                        "pm1.0：{}，" +
                        "pm2.5：{}，" +
                        "pm10：{}，" +
                        "甲醛：{}，" +
                        "温度：{}，" +
                        "湿度：{}，" +
                        "一氧化碳：{}，" +
                        "二氧化碳：{}，" +
                        "一氧化氮：{}，" +
                        "二氧化氮：{}，" +
                        "臭氧：{}，" +
                        "二氧化硫：{}，" +
                        "有机气态物质：{}，" +
                        "风速：{}，" +
                        "风向：{}，" +
                        "经度：{}，" +
                        "纬度：{}，" +
                        "太阳能电源电量：{}",
                        packet.getUniqueMark(),
                        collectTime,
                        sim,
                        pm1_0,
                        pm2_5,
                        pm10,
                        formaldehyde,
                        temperature,
                        humidity,
                        co,
                        co2,
                        no,
                        no2,
                        o3,
                        so2,
                        tvoc,
                        windSpeed,
                        windDirection,
                        longitude,
                        latitude,
                        electricity);
            }else{
                logger.info("该设备信息不存在或未进行登入，设备标识码为：{}" + packet.getUniqueMark());
                ctx.close();
            }

        } catch (Exception ex) {
            logger.error("解析实时上报信息出错:" + ex);
        }
    }
}
