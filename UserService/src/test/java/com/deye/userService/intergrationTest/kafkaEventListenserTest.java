package com.deye.userService.intergrationTest;

import com.deye.userService.event.OrderCreatedEvent;
import com.deye.userService.event.UserValidatedEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class kafkaEventListenserTest {
    private final BlockingQueue<ConsumerRecord<String, UserValidatedEvent>> userValidatedEventBlockingQueue = new ArrayBlockingQueue<>(1);

    @KafkaListener(topics = "user.event", groupId = "test")
    public void listen(ConsumerRecord<String, UserValidatedEvent> record) {
        userValidatedEventBlockingQueue.offer(record);
    }

    public ConsumerRecord<String, UserValidatedEvent> getUserValidatedEvent() throws InterruptedException {
        return userValidatedEventBlockingQueue.poll(10, TimeUnit.SECONDS);
    }
}
