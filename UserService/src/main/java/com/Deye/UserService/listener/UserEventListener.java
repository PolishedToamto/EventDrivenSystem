package com.Deye.UserService.listener;

import com.Deye.UserService.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {

    @KafkaListener(topics = "order-Created-event")
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("UserService received: " + event.getOrderId());
        // later: enrichment, audit, user profiling
    }
}
