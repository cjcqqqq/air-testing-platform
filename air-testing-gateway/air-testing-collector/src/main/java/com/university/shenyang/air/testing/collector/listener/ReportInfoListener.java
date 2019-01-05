package com.university.shenyang.air.testing.collector.listener;

import com.university.shenyang.air.testing.collector.util.Constants;
import com.university.shenyang.air.testing.model.ReportInfo;
import net.sf.json.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 消费kafka中的设备上报数据
 */
@Component
public class ReportInfoListener {
    public static Logger logger = LoggerFactory.getLogger(ReportInfoListener.class);

    @Autowired
    public RedisTemplate<String, ReportInfo> redisTemplate;

    @KafkaListener(topics = "${collector.topic.name:collector01}")
    public void listen(ConsumerRecord<?, ?> record) {
        try {
            String value = (String) record.value();
            JSONObject sfObject = JSONObject.fromObject(value);
            ReportInfo reportInfo = (ReportInfo) JSONObject.toBean(sfObject,ReportInfo.class);
            // 上报数据添加至redis缓存，隔天凌晨转存mysql
            redisTemplate.opsForZSet().add(Constants.REPORT_REDIS_KEY_PREFIX + reportInfo.getDeviceCode(), reportInfo, reportInfo.getCollectTime().getTime());
            // 更新最新上报数据
            redisTemplate.opsForValue().set(Constants.LATEST_REPORT_REDIS_KEY_PREFIX + reportInfo.getDeviceCode(), reportInfo);

            logger.info("收到设备实时采集数据 设备识别码{}，" +
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
                    reportInfo.getDeviceCode(),
                    reportInfo.getCollectTime(),
                    reportInfo.getSim(),
                    reportInfo.getPm1_0(),
                    reportInfo.getPm2_5(),
                    reportInfo.getPm10(),
                    reportInfo.getFormaldehyde(),
                    reportInfo.getTemperature(),
                    reportInfo.getHumidity(),
                    reportInfo.getCo(),
                    reportInfo.getCo2(),
                    reportInfo.getNo(),
                    reportInfo.getNo2(),
                    reportInfo.getO3(),
                    reportInfo.getSo2(),
                    reportInfo.getTvoc(),
                    reportInfo.getWindSpeed(),
                    reportInfo.getWindDirection(),
                    reportInfo.getLongitude(),
                    reportInfo.getLatitude(),
                    reportInfo.getElectricity());
        } catch (Exception e) {
            logger.error("kafka数据解析错误", e);
        }
    }
}
