package com.Deye.NotificationService.integrationTest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class TestDLQListener {

    private final CountDownLatch latch = new CountDownLatch(1);
    private final Logger logger = LoggerFactory.getLogger(TestDLQListener.class);
    private ConsumerRecord<String, Object> receivedRecord;

    @KafkaListener(topics = "user.event.dlt", groupId = "test")
    public void listen(ConsumerRecord<String, Object> record) {
        this.receivedRecord = record;
        latch.countDown();
    }

    public boolean awaitMessage() throws InterruptedException {
        return latch.await(15, TimeUnit.SECONDS);
    }

    public ConsumerRecord<String, Object> getReceivedMessage() {
        return receivedRecord;
    }

    public void reset(){
        this.receivedRecord = null;
    }
}