package com.deye.userService.unitTest;

import com.deye.userService.event.OrderCreatedEvent;
import com.deye.userService.listener.UserEventListener;
import com.deye.userService.producer.UserEventProducer;
import com.deye.userService.service.UserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserEventListenerTest {
    @Mock
    private UserEventProducer userEventProducer;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserEventListener userEventListener;

    @Test
    public void testUserEventListener() {
        OrderCreatedEvent createdEvent = new OrderCreatedEvent();
        createdEvent.setUserId(0);
        createdEvent.setOrderId(1);
        createdEvent.setProductName("test");
        createdEvent.setQuantity(1);
        createdEvent.setPrices(new BigDecimal("15.99"));
        createdEvent.setEmail("test@gmail.com");

        when(userService.isValidUser(eq(0))).thenReturn(true);
        when(userService.isValidEmail(argThat(email->email.contains("@")))).thenReturn(true);

        ConsumerRecord<String, OrderCreatedEvent> record = new ConsumerRecord<>("order.event", 0, 0, "0", createdEvent);
        record.headers().add("X-Correlation-Id", UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));

        userEventListener.handleOrderCreated(record);

        verify(userService).isValidUser(eq(0));
        verify(userService).isValidEmail(eq(createdEvent.getEmail()));

        verify(userEventProducer).sendUserEvent(argThat(
                (event) -> event.getUserId() == 0 && event.getEmail().equalsIgnoreCase("test@gmail.com")), eq(true));
    }
}
