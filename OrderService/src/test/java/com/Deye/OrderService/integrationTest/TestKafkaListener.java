package com.Deye.OrderService.integrationTest;

import com.Deye.OrderService.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class TestKafkaListener {

    private final BlockingQueue<OrderCreatedEvent> messages = new LinkedBlockingQueue<>();

    @KafkaListener(topics = "order.event", groupId = "test")
    public void listen(OrderCreatedEvent event) {
        messages.add(event);
    }

    public OrderCreatedEvent waitForMessage() throws InterruptedException {
        return messages.poll(5, TimeUnit.SECONDS);
    }

    public int getMessageSize(){
        return messages.size();
    }
}