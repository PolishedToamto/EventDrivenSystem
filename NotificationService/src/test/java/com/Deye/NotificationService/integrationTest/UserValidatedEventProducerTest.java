package com.Deye.NotificationService.integrationTest;

import com.Deye.NotificationService.domain.event.UserValidatedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class UserValidatedEventProducerTest {
    private final KafkaTemplate<String, UserValidatedEvent> kafkaTemplate;

    public UserValidatedEventProducerTest(KafkaTemplate<String, UserValidatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void producedEvent(String correlationId){
        ProducerRecord<String, UserValidatedEvent> record = new ProducerRecord<>("user.event", 0, "0", new UserValidatedEvent(0,0,true));
        record.headers().add("X-Correlation-Id", correlationId.getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);
    }
}
