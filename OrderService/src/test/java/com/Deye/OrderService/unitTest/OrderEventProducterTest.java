package com.Deye.OrderService.unitTest;

import com.Deye.OrderService.event.OrderCreatedEvent;
import com.Deye.OrderService.producer.OrderEventProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderEventProducterTest {
    @InjectMocks
    private OrderEventProducer orderEventProducer;

    @Mock
    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    /*
    &&  ((OrderCreatedEvent)(e)).getUserId().equals(event.getUserId())
                &&  ((OrderCreatedEvent)(e)).getProductName().equals(event.getProductName())
                &&  ((OrderCreatedEvent)(e)).getQuantity().equals(event.getQuantity())
                &&  ((OrderCreatedEvent)(e)).getPrices().equals(event.getPrices())
                &&  ((OrderCreatedEvent)(e)).getEmail().equals(event.getEmail())
     */
    @Test
    public void sendOrderCreatedEvent(){
        OrderCreatedEvent event = new OrderCreatedEvent(1, 0, "test", 1, new BigDecimal(15.99), "test@gmail.com");

        orderEventProducer.sendOrderCreatedEvent(event);

        ArgumentCaptor<ProducerRecord<String, OrderCreatedEvent>> captor =
                ArgumentCaptor.forClass(ProducerRecord.class);

        verify(kafkaTemplate).send(captor.capture());
        ProducerRecord<String, OrderCreatedEvent> record = captor.getValue();

        OrderCreatedEvent value = record.value();

        assertEquals(event.getOrderId(), value.getOrderId());
        assertEquals(event.getUserId(), value.getUserId());
        assertEquals(event.getProductName(), value.getProductName());
        assertEquals(event.getQuantity(), value.getQuantity());
        assertEquals(event.getPrices(), value.getPrices());
        assertEquals(event.getEmail(), value.getEmail());

    }
}
