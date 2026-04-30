package com.deye.userService.producer;

import com.deye.userService.event.OrderCreatedEvent;
import com.deye.userService.event.UserValidatedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserEventProducer {
    private final KafkaTemplate<String, UserValidatedEvent> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(UserEventProducer.class);

    public UserEventProducer(KafkaTemplate<String, UserValidatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserEvent(OrderCreatedEvent orderCreatedEvent, boolean isValid) {
        logger.info("Entering UserEventProducer.sendUserEvent()");

        UserValidatedEvent userValidatedEvent = new UserValidatedEvent(orderCreatedEvent.getOrderId(), orderCreatedEvent.getUserId(), orderCreatedEvent.getEmail(), isValid);

        ProducerRecord<String, UserValidatedEvent> record = new ProducerRecord<String, UserValidatedEvent>("user.event", userValidatedEvent);

        String correlationId = MDC.get("correlationId");

        if(correlationId != null) {
            record.headers().add("X-Correlation-Id", correlationId.getBytes());
        }

        kafkaTemplate.send(record);

        logger.info("Exiting UserEventProducer.sendUserEvent()");
    }
}
