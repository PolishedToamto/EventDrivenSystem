package com.Deye.NotificationService.listener;

import com.Deye.NotificationService.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventListener {

    @KafkaListener(topics = "order-Created-event")
    public void sendNotification(OrderCreatedEvent event) {
        System.out.println("Sending notification for order: " + event.getOrderId());
        // simulate email/SMS
    }
}