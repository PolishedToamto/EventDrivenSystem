package com.Deye.NotificationService.integrationTest;

import com.Deye.NotificationService.domain.event.UserValidatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TestFailingListener {

    private final AtomicInteger attempts = new AtomicInteger(0);
    private final Logger logger = LoggerFactory.getLogger(TestFailingListener.class);

    public TestFailingListener() {
        logger.info("TestFailingListener CREATED");
    }

    @KafkaListener(topics = "user.event", groupId = "test")
    public void listen(UserValidatedEvent event) {
        logger.info("Received UserValidatedEvent: " + event.getUserId());
        if(event.getValid() == false){
            attempts.incrementAndGet();
            throw new RuntimeException("Force failure for DLQ test");
        }
    }

    public int getAttempts() {
        return attempts.get();
    }

    public void reset(){
        this.attempts.set(0);
    }
}