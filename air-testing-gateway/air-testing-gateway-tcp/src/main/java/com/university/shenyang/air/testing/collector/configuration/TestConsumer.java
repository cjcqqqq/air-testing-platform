package com.university.shenyang.air.testing.collector.configuration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * kafka消费者测试
 */
@Component
public class TestConsumer {

    @KafkaListener(topics = "${reportinfo.topic.name:reportinfo}")
    public void listen(ConsumerRecord<?, ?> record) {
        System.out.println("接收消息为:" + record.value());
    }
}
