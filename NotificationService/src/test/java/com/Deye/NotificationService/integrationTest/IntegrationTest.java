package com.Deye.NotificationService.integrationTest;

import com.Deye.NotificationService.domain.event.UserValidatedEvent;
import com.Deye.NotificationService.infrastructure.email.AwsSesSmtpEmailSender;
import com.Deye.NotificationService.infrastructure.kafka.listener.NotificationEventListener;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class IntegrationTest {

    @Container
    static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");


    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private TestFailingListener failingListener;

    @Autowired
    private TestDLQListener dlqListener;

    @Autowired
    private UserValidatedEventProducerTest eventProducerTest;

    @MockitoBean
    private SesClient sesClient;

    //this one execute once
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        // priority from high to low
        // dynamicPropertySource -> application-test-yml -> application.yml
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        failingListener.reset();
        dlqListener.reset();
    }

    @Test
    public void shouldSendMessageToDLQAfterRetries() throws Exception {
        String correlationId = UUID.randomUUID().toString();
        // send test event
        ProducerRecord<String, Object> record = new ProducerRecord<>("user.event", 0,"0", new UserValidatedEvent(0,0,false));
        record.headers().add("X-Correlation-Id", correlationId.getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);

        // assert DLQ received message, and await for maximum attempt
        assertTrue(dlqListener.awaitMessage(), "Message was not sent to DLQ");

        // assert retry attempts (1 initial + 3 retries = 4)
        assertEquals(4, failingListener.getAttempts());

        // optional: verify payload exists
        assertNotNull(dlqListener.getReceivedMessage());

        //validate correlation id
        assertEquals(correlationId, new String(dlqListener.getReceivedMessage().headers().lastHeader("X-Correlation-Id").value()));
    }

    @Test
    public void wholeFlowTest(){
        String correlationId = UUID.randomUUID().toString();
        eventProducerTest.producedEvent(correlationId);

        await().atMost(5, SECONDS).untilAsserted(() ->
                verify(sesClient, times(1)).sendEmail((SendEmailRequest) any())
        );

    }

}