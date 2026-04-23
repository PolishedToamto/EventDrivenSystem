package com.deye.userService.intergrationTest;

import com.deye.userService.event.UserValidatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class kafkaEventListenserTest {
    private final BlockingQueue<UserValidatedEvent> userValidatedEventBlockingQueue = new ArrayBlockingQueue<>(1);

    @KafkaListener(topics = "user.event", groupId = "test")
    public void listen(UserValidatedEvent event){
        userValidatedEventBlockingQueue.offer(event);
    }

    public UserValidatedEvent getUserValidatedEvent() throws InterruptedException {
        return userValidatedEventBlockingQueue.poll(10, TimeUnit.SECONDS);
    }
}
