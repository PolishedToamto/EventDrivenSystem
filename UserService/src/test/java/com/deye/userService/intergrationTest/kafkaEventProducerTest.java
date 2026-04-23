package com.deye.userService.intergrationTest;

import com.deye.userService.event.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class kafkaEventProducerTest {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public kafkaEventProducerTest(KafkaTemplate<String, Object> kafkaTemplate) {
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

        kafkaTemplate.send("order.event", orderCreatedEvent);
    }

    public void sendInValidEvent(){
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.setOrderId(1);
        orderCreatedEvent.setUserId(-2);
        orderCreatedEvent.setProductName("test");
        orderCreatedEvent.setPrices(new BigDecimal("15.99"));
        orderCreatedEvent.setQuantity(1);
        orderCreatedEvent.setEmail("");

        kafkaTemplate.send("order.event", orderCreatedEvent);
    }
}
