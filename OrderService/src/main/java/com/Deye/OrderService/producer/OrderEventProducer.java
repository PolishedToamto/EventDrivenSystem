package com.Deye.OrderService.producer;

import com.Deye.OrderService.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        kafkaTemplate.send("order.event", event);
        logger.info("order sent with id " + event.getOrderId());
        logger.info("order sent with user id " + event.getUserId());
    }
}
