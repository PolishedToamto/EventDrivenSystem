package com.Deye.NotificationService.integrationTest;

import com.Deye.NotificationService.domain.event.UserValidatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TestFailingListener {

    private final AtomicInteger attempts = new AtomicInteger(0);

    public TestFailingListener() {
        System.out.println("TestFailingListener CREATED");
    }

    @KafkaListener(topics = "user.event", groupId = "test")
    public void listen(UserValidatedEvent event) {
        System.out.println("Received UserValidatedEvent: " + event.getUserId());
        attempts.incrementAndGet();
        throw new RuntimeException("Force failure for DLQ test");
    }

    public int getAttempts() {
        return attempts.get();
    }
}