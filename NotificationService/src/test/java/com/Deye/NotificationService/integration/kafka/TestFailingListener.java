package com.Deye.NotificationService.integration.kafka;

import com.Deye.NotificationService.event.UserValidatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TestFailingListener {

    private final AtomicInteger attempts = new AtomicInteger(0);

    @KafkaListener(topics = "user.event", groupId = "test")
    public void listen(UserValidatedEvent event) {
        attempts.incrementAndGet();
        throw new RuntimeException("Force failure for DLQ test");
    }

    public int getAttempts() {
        return attempts.get();
    }
}