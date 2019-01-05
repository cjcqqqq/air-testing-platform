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
        } catch (Exception e) {
            logger.error("kafka数据解析错误", e);
        }
    }
}
