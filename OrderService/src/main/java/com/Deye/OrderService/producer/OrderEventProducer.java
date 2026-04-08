package com.Deye.OrderService.producer;

import com.Deye.OrderService.event.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(OrderCreatedEvent event){

        kafkaTemplate.send("order.event", event);
        System.out.println("order sent with id " + event.getOrderId());
        System.out.println("order sent with user id " + event.getUserId());
    }
}
