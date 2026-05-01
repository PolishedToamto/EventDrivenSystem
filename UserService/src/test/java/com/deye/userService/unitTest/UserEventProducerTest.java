package com.deye.userService.unitTest;

import com.deye.userService.event.OrderCreatedEvent;
import com.deye.userService.event.UserValidatedEvent;
import com.deye.userService.producer.UserEventProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserEventProducerTest {
    @InjectMocks
    private UserEventProducer userEventProducer;

    @Mock
    private KafkaTemplate<String, UserValidatedEvent> kafkaTemplate;

    @Test
    public void sendUserEventTest(){
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.setUserId(1);
        orderCreatedEvent.setOrderId(1);
        orderCreatedEvent.setProductName("productName");
        orderCreatedEvent.setEmail("test@gmail.com");
        orderCreatedEvent.setPrices(new BigDecimal("15.99"));
        orderCreatedEvent.setQuantity(1);

        userEventProducer.sendUserEvent(orderCreatedEvent, true);

        ArgumentCaptor<ProducerRecord<String, UserValidatedEvent>> captor = ArgumentCaptor.forClass(ProducerRecord.class);

        verify(kafkaTemplate).send(captor.capture());

        ProducerRecord<String, UserValidatedEvent> producerRecord = captor.getValue();

        Assertions.assertEquals(1, producerRecord.value().getUserId());
        Assertions.assertEquals("test@gmail.com", producerRecord.value().getEmail());
    }
}
