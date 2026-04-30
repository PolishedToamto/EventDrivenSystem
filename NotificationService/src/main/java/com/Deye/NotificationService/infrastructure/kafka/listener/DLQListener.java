package com.Deye.NotificationService.infrastructure.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DLQListener {
    private final Logger logger = LoggerFactory.getLogger(DLQListener.class);

    @KafkaListener(topics = "user.event.dlt")
    void listen(ConsumerRecord<String, Object> record) {
        String correlationId = new String(record.headers().lastHeader("X-Correlation-Id").value());
        logger.warn("DLQ received: topic={}, key={}, correlationId={}",
                record.topic(),
                record.key(),
                correlationId);

        logger.warn("DLQ payload={}", record.value());

        record.headers().forEach(h ->
                logger.warn("header {}={}",
                        h.key(),
                        new String(h.value()))
        );
    }
}
