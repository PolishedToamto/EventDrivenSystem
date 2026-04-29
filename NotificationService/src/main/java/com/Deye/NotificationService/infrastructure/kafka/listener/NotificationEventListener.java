package com.Deye.NotificationService.infrastructure.kafka.listener;

import com.Deye.NotificationService.domain.event.UserValidatedEvent;
import com.Deye.NotificationService.application.service.EmailService;
import com.Deye.NotificationService.application.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventListener {
    NotificationService notificationService;

    EmailService emailService;
    final Logger logger = LoggerFactory.getLogger(NotificationEventListener.class);

    public NotificationEventListener(NotificationService notificationService, EmailService emailService) {
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user.event")
    public void sendNotification(UserValidatedEvent event) {
        emailService.sendEmail("dleipersonal@gmail.com", "Test Email", "Hello this is a test email!");
        notificationService.sendNotification(event.getUserId(), "hello world");
    }
}