package com.Deye.NotificationService.listener;

import com.Deye.NotificationService.event.UserValidatedEvent;
import com.Deye.NotificationService.service.EmailService;
import com.Deye.NotificationService.service.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.backoff.FixedBackOff;

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