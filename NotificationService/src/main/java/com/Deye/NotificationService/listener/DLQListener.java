package com.Deye.NotificationService.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DLQListener {
    @KafkaListener(topics = "user.event.dlt")
    void listen(Object message){
        //process latter
    }
}
