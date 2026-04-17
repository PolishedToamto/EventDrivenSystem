package com.Deye.UserService.producer;

import com.Deye.UserService.event.OrderCreatedEvent;
import com.Deye.UserService.event.UserValidatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserEvent(OrderCreatedEvent orderCreatedEvent, boolean isValid) {
        UserValidatedEvent userValidatedEvent = new UserValidatedEvent(orderCreatedEvent.getOrderId(), orderCreatedEvent.getUserId(), orderCreatedEvent.getEmail(), isValid);
        kafkaTemplate.send("user.event", userValidatedEvent);
    }
}
