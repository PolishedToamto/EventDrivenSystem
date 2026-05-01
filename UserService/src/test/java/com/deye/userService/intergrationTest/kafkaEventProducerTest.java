package com.deye.userService.intergrationTest;

import com.deye.userService.event.OrderCreatedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class kafkaEventProducerTest {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public kafkaEventProducerTest(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendValidEvent(){
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.setOrderId(1);
        orderCreatedEvent.setUserId(1);
        orderCreatedEvent.setProductName("test");
        orderCreatedEvent.setPrices(new BigDecimal("15.99"));
        orderCreatedEvent.setQuantity(1);
        orderCreatedEvent.setEmail("test@gmail.com");

        ProducerRecord<String, OrderCreatedEvent> record = new ProducerRecord<>("order.event", orderCreatedEvent);
        record.headers().add("X-Correlation-Id", UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);
    }

    public void sendInValidEvent(){
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.setOrderId(1);
        orderCreatedEvent.setUserId(-2);
        orderCreatedEvent.setProductName("test");
        orderCreatedEvent.setPrices(new BigDecimal("15.99"));
        orderCreatedEvent.setQuantity(1);
        orderCreatedEvent.setEmail("");

        ProducerRecord<String, OrderCreatedEvent> record = new ProducerRecord<>("order.event", orderCreatedEvent);
        record.headers().add("X-Correlation-Id", UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);
    }
}
