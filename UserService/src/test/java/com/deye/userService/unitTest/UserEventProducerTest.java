package com.deye.userService.unitTest;

import com.deye.userService.event.OrderCreatedEvent;
import com.deye.userService.event.UserValidatedEvent;
import com.deye.userService.producer.UserEventProducer;
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

        verify(kafkaTemplate).send(eq("user.event"),
                (argThat(e-> e.getUserId().equals(1) && e.getEmail().equalsIgnoreCase("test@gmail.com"))));
    }
}
