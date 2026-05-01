package com.Deye.OrderService.integrationTest;

import com.Deye.OrderService.entity.Order;
import com.Deye.OrderService.event.OrderCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class TestKafkaListener {

    private final BlockingQueue<ConsumerRecord<String, OrderCreatedEvent>> messages = new LinkedBlockingQueue<>();

    @KafkaListener(topics = "order.event", groupId = "test")
    public void listen(ConsumerRecord<String, OrderCreatedEvent> event) {
        messages.add(event);
    }

    public ConsumerRecord<String, OrderCreatedEvent> waitForMessage() throws InterruptedException {
        return messages.poll(5, TimeUnit.SECONDS);
    }

    public int getMessageSize(){
        return messages.size();
    }
}