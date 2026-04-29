package com.deye.userService.intergrationTest;


import com.deye.userService.event.UserValidatedEvent;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public class IntegrationTest {
    @Container
    static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private kafkaEventProducerTest startEventProducerTest;

    @Autowired
    private kafkaEventListenserTest endEventListenerTest;

    @Autowired
    private Environment environment;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    public void testFileLoad(){
        System.out.println(environment.getProperty("test.marker"));
    }

    @Test
    public void integrationTest() throws InterruptedException {
        startEventProducerTest.sendValidEvent();

        UserValidatedEvent validEvent = endEventListenerTest.getUserValidatedEvent();

        Assert.assertNotNull(validEvent);
        Assertions.assertEquals("test@gmail.com", validEvent.getEmail());
        Assertions.assertEquals(true, validEvent.getValid());

        startEventProducerTest.sendInValidEvent();

        UserValidatedEvent invalidEvent = endEventListenerTest.getUserValidatedEvent();

        Assert.assertNotNull(invalidEvent);
        Assertions.assertEquals(false, invalidEvent.getValid());
    }
}
