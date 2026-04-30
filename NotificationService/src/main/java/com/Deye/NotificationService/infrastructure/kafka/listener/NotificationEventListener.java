package com.Deye.NotificationService.infrastructure.kafka.listener;

import com.Deye.NotificationService.domain.event.UserValidatedEvent;
import com.Deye.NotificationService.application.service.EmailService;
import com.Deye.NotificationService.application.service.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventListener {
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final Logger logger = LoggerFactory.getLogger(NotificationEventListener.class);

    public NotificationEventListener(NotificationService notificationService, EmailService emailService) {
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user.event")
    public void sendNotification(ConsumerRecord<String, UserValidatedEvent> record) {
        String correlationId = new String(record.headers().lastHeader("X-Correlation-Id").value());

        MDC.put("correlationId", correlationId);

        logger.info("Entering NotificationEventListener.sendNotification()");
        logger.info("CorrelationId: " + correlationId);

        UserValidatedEvent event = record.value();

        emailService.sendEmail("dleipersonal@gmail.com", "Test Email", "Hello this is a test email!");
        notificationService.sendNotification(event.getUserId(), "hello world");

        logger.info("Exiting NotificationEventListener.sendNotification()");
        MDC.clear();
    }
}