package com.shangan.trade.coupon.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageSender {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 发送消息
     * @param topic
     * @param message
     */
    public void sendMessage(String topic, String message) {
        log.info("send message topic:{} message:{}", topic, message);
        kafkaTemplate.send(topic, message);
    }
}
