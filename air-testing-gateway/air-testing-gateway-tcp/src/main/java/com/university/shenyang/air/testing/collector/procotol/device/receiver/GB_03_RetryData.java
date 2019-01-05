package com.university.shenyang.air.testing.collector.procotol.device.receiver;


import com.university.shenyang.air.testing.collector.common.kit.Convert;
import com.university.shenyang.air.testing.collector.common.kit.Packet;
import com.university.shenyang.air.testing.collector.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.collector.cache.DevicesManager;
import com.university.shenyang.air.testing.collector.procotol.Protocols;
import com.university.shenyang.air.testing.collector.procotol.device.DeviceCommand;
import com.university.shenyang.air.testing.collector.util.Constants;
import com.university.shenyang.air.testing.model.ReportInfo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * 终端补发数据，调用02的处理流程。
 * Created by chenjc on 2017/05/03.
 */
@Protocols(id = "03", type = Constants.PROTOCOL_GB)
@ChannelHandler.Sharable
public class GB_03_RetryData extends DeviceCommand {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(GB_03_RetryData.class);

    @Autowired
    public RedisTemplate<String, ReportInfo> redisTemplate;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${reportinfo.topic.name:reportinfo}")
    private String reportTopic;

    @Override
    public void processor(ChannelHandlerContext ctx, Packet packet) {
        try {
            // 获取登入链路上下文
            ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(packet.getUniqueMark());

            // 判断设备是否合法并登入
            if (loginCtx == ctx) {

                byte[] content = ArraysUtils.subarrays(packet.getContent(), 0);//复制一份数组，下边内容进行解析时会进行截断。

                // 设备消息通用应答
                packet = super.terminalMessageCommonAnswer(packet);
                // 发送应答
                ctx.writeAndFlush(packet);

                ReportInfo reportInfo = new ReportInfo();

                // code
                String code = packet.getUniqueMark();
                reportInfo.setDeviceCode(code);
                // collectTime; // 采集时间 6个字节
                String collectTime = Convert.gbDateToString(ArraysUtils.subarrays(content, 0, 6));
                reportInfo.setCollectTime(Convert.strToDate(collectTime));
                // sim; //sim卡号 20位 4个字节
                String sim = new String(ArraysUtils.subarrays(content, 6, 20), "ASCII");
                reportInfo.setSim(sim);
//                long sim = Convert.byte2Long(ArraysUtils.subarrays(content, 6, 4), 4);
                // pm1_0; // pm1.0 单位:微克/立方米 整数 4个字节
                int pm1_0 = Convert.byte2Int(ArraysUtils.subarrays(content, 26, 4), 4);
                reportInfo.setPm1_0(pm1_0);
                // pm2_5; // pm2.5 单位:微克/立方米 整数 4个字节
                int pm2_5 = Convert.byte2Int(ArraysUtils.subarrays(content, 30, 4), 4);
                reportInfo.setPm2_5(pm2_5);
                // pm10;      // pm10  单位:微克/立方米 整数 4个字节
                int pm10 = Convert.byte2Int(ArraysUtils.subarrays(content, 34, 4), 4);
                reportInfo.setPm10(pm10);
                // formaldehyde; // 甲醛 0.00-9.99 * 100 2个字节
                int formaldehyde = Convert.byte2Int(ArraysUtils.subarrays(content, 38, 2), 2);
                reportInfo.setFormaldehyde(((float) formaldehyde) / 100);
                // temperature; // 温度 0-255 * 10 偏移500 0表示-50摄氏度 2个字节
                int temperature = Convert.byte2Int(ArraysUtils.subarrays(content, 40, 2), 2);
                reportInfo.setTemperature(Float.valueOf(temperature - 500) / 10);
                // humidity; // 湿度 0-100 1个字节
                int humidity = Convert.byte2Int(ArraysUtils.subarrays(content, 42, 1), 1);
                reportInfo.setHumidity(humidity);
                // co; // 一氧化碳 预留4个字节
                int co = Convert.byte2Int(ArraysUtils.subarrays(content, 43, 4), 4);
                reportInfo.setCo(co);
                // co2; // 二氧化碳 预留4个字节
                int co2 = Convert.byte2Int(ArraysUtils.subarrays(content, 47, 4), 4);
                reportInfo.setCo2(co2);
                // no; // 一氧化氮 预留4个字节
                int no = Convert.byte2Int(ArraysUtils.subarrays(content, 51, 4), 4);
                reportInfo.setNo(no);
                // no2; // 二氧化氮 预留4个字节
                int no2 = Convert.byte2Int(ArraysUtils.subarrays(content, 55, 4), 4);
                reportInfo.setNo2(no2);
                // o3; // 臭氧 预留4个字节
                int o3 = Convert.byte2Int(ArraysUtils.subarrays(content, 59, 4), 4);
                reportInfo.setO3(o3);
                // so2; // 二氧化硫 预留4个字节
                int so2 = Convert.byte2Int(ArraysUtils.subarrays(content, 63, 4), 4);
                reportInfo.setSo2(so2);
                // tvoc; // 有机气态物质 预留4个字节
                int tvoc = Convert.byte2Int(ArraysUtils.subarrays(content, 67, 4), 4);
                reportInfo.setTvoc(tvoc);
                // windSpeed; // 风速 预留4个字节
                int windSpeed = Convert.byte2Int(ArraysUtils.subarrays(content, 71, 4), 4);
                reportInfo.setWindSpeed(windSpeed);
                // windDirection; // 风向 预留2个字节
                int windDirection = Convert.byte2Int(ArraysUtils.subarrays(content, 75, 2), 2);
                reportInfo.setWindDirection(windDirection);
                // longitude; // 经度 以度为单位的经度值乘以10的6次方，精确到百万分之一度。 4个字节
                long longitude = Convert.byte2Long(ArraysUtils.subarrays(content, 77, 4), 4);
                reportInfo.setLongitude(((float) longitude) / 1000000);
                // latitude; // 纬度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度。 4个字节
                long latitude = Convert.byte2Long(ArraysUtils.subarrays(content, 81, 4), 4);
                reportInfo.setLatitude(((float) latitude) / 1000000);
                // electricity; // 太阳能电源电量0-100 1个字节
                int electricity = Convert.byte2Int(ArraysUtils.subarrays(content, 85, 1), 1);
                reportInfo.setElectricity(electricity);

                // 上报数据添加至redis缓存，隔天凌晨转存mysql
                redisTemplate.opsForZSet().add(Constants.REPORT_REDIS_KEY_PREFIX + code, reportInfo, reportInfo.getCollectTime().getTime());
                // 更新最新上报数据
                redisTemplate.opsForValue().set(Constants.LATEST_REPORT_REDIS_KEY_PREFIX + code, reportInfo);

                // 上报数据写入kafka
                JSONObject object = JSONObject.fromObject(reportInfo);
                kafkaTemplate.send(reportTopic, reportInfo.getDeviceCode(), object.toString());

                LOGGER.debug("收到设备补传采集数据 设备识别码{}，" +
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
            } else {
                LOGGER.info("该设备信息不存在或未进行登入，设备标识码为：{}" + packet.getUniqueMark());
                ctx.close();
            }

        } catch (Exception ex) {
            LOGGER.error("解析补传上报信息出错:" + ex);
        }
    }
}
