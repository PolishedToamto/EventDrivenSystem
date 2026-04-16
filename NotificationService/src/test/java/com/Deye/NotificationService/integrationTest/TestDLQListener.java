package com.Deye.NotificationService.integrationTest;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class TestDLQListener {

    private final CountDownLatch latch = new CountDownLatch(1);
    private Object receivedMessage;

    public TestDLQListener() {
        System.out.println("TestDLQListener created");
    }

    @KafkaListener(topics = "user.event.dlt", groupId = "test")
    public void listen(Object message) {
        this.receivedMessage = message;
        latch.countDown();
    }

    public boolean awaitMessage() throws InterruptedException {
        return latch.await(20, TimeUnit.SECONDS);
    }

    public Object getReceivedMessage() {
        return receivedMessage;
    }
}