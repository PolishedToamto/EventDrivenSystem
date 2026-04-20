package com.Deye.OrderService.unitTest;

import com.Deye.OrderService.event.OrderCreatedEvent;
import com.Deye.OrderService.producer.OrderEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderEventProducterTest {
    @InjectMocks
    private OrderEventProducer orderEventProducer;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    public void sendOrderCreatedEvent(){
        OrderCreatedEvent event = new OrderCreatedEvent(1, 0, "test", 1, new BigDecimal(15.99), "test@gmail.com");

        orderEventProducer.sendOrderCreatedEvent(event);

        verify(kafkaTemplate).send(eq("order.event"), argThat(
                e -> ((OrderCreatedEvent)(e)).getOrderId().equals( event.getOrderId())
                &&  ((OrderCreatedEvent)(e)).getUserId().equals(event.getUserId())
                &&  ((OrderCreatedEvent)(e)).getProductName().equals(event.getProductName())
                &&  ((OrderCreatedEvent)(e)).getQuantity().equals(event.getQuantity())
                &&  ((OrderCreatedEvent)(e)).getPrices().equals(event.getPrices())
                &&  ((OrderCreatedEvent)(e)).getEmail().equals(event.getEmail())
        ));

    }
}
