package com.Deye.OrderService.producer;

import com.Deye.OrderService.event.OrderCreatedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(OrderEventProducer.class);
    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(OrderCreatedEvent event){
        logger.info("Entering sendOrderCreatedEvent()");
        String correlationId = MDC.get("correlationId");

        ProducerRecord<String, Object> record =
                new ProducerRecord<>("order.event", event);

        if (correlationId != null) {
            record.headers().add(
                    "X-Correlation-Id",
                    correlationId.getBytes()
            );
        }

        kafkaTemplate.send(record);

        logger.info("order sent with id " + event.getOrderId());
        logger.info("order sent with user id " + event.getUserId());
        logger.info("Exiting sendOrderCreatedEvent()");
    }
}
