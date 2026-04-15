package com.Deye.NotificationService.integration.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class TestDLQListener {

    private final CountDownLatch latch = new CountDownLatch(1);
    private Object receivedMessage;

    @KafkaListener(topics = "user.event.dlt")
    public void listen(Object message) {
        this.receivedMessage = message;
        latch.countDown();
    }

    public boolean awaitMessage() throws InterruptedException {
        return latch.await(10, TimeUnit.SECONDS);
    }

    public Object getReceivedMessage() {
        return receivedMessage;
    }
}