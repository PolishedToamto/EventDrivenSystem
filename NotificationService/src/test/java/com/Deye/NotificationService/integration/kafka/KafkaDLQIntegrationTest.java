package com.Deye.NotificationService.integration.kafka;

import com.Deye.NotificationService.event.UserValidatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {"user.event", "user.event.dlt"}
)
class KafkaDLQIntegrationTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private TestFailingListener failingListener;

    @Autowired
    private TestDLQListener dlqListener;

    @Test
    public void shouldSendMessageToDLQAfterRetries() throws Exception {

        // send test event
        kafkaTemplate.send("user.event", new UserValidatedEvent(0,0,true));
        System.out.println("first test pass");
        // assert DLQ received message
        assertTrue(dlqListener.awaitMessage(), "Message was not sent to DLQ");
        System.out.println("second test pass");
        // assert retry attempts (1 initial + 3 retries = 4)
        assertEquals(4, failingListener.getAttempts());
        System.out.println("third test pass");
        // optional: verify payload exists
        assertNotNull(dlqListener.getReceivedMessage());
        System.out.println("fourth test pass");
    }
}