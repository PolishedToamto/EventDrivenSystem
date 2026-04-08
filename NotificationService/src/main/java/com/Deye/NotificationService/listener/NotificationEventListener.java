package com.Deye.NotificationService.listener;

import com.Deye.NotificationService.event.OrderCreatedEvent;
import com.Deye.NotificationService.event.UserValidatedEvent;
import com.Deye.NotificationService.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventListener {
    NotificationService notificationService;

    public NotificationEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "user.event")
    public void sendNotification(UserValidatedEvent event) {
        System.out.println("listening notification for user: " + event.getOrderId());

        notificationService.sendNotification(event.getUserId(), "hello world");
    }
}