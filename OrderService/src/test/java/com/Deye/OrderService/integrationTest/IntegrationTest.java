package com.Deye.OrderService.integrationTest;

import com.Deye.OrderService.dto.OrderRequest;
import com.Deye.OrderService.entity.Order;
import com.Deye.OrderService.event.OrderCreatedEvent;
import com.Deye.OrderService.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
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
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestKafkaListener kafkaListener;

    @Autowired
    Environment env;

    @Test
    void debug() {
        //testing if application-test yml is loaded
        System.out.println(env.getProperty("test.marker"));
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    public void createOrderIntegrationTest() throws InterruptedException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(0);
        orderRequest.setProductName("testProduct");
        orderRequest.setQuantity(1);
        orderRequest.setPrice(new BigDecimal("15.99"));
        orderRequest.setEmail("test@gmail.com");

        ResponseEntity<com.Deye.OrderService.entity.Order> response = restTemplate.postForEntity("/orders", orderRequest, Order.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        com.Deye.OrderService.entity.Order saved = orderRepository.findById(response.getBody().getOrderId()).orElse(null);

        assertNotNull(saved);

        assertEquals(response.getBody().getUserId(), saved.getUserId());
        assertEquals("testProduct", saved.getProductName());
        assertEquals(1, saved.getQuantity());
        assertTrue(new BigDecimal("15.99").compareTo(saved.getPrice()) == 0);
        assertEquals(response.getBody().getEmail(), saved.getEmail());

        OrderCreatedEvent orderCreatedEvent = kafkaListener.waitForMessage();
        assertNotNull(orderCreatedEvent);

        assertEquals("testProduct", orderCreatedEvent.getProductName());
    }

    @Test
    public void getOrderByIdIntegrationTest(){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(0);
        orderRequest.setProductName("testProduct");
        orderRequest.setQuantity(1);
        orderRequest.setPrice(new BigDecimal("15.99"));
        orderRequest.setEmail("test@gmail.com");

        ResponseEntity<Order> response1 = restTemplate.postForEntity("/orders", orderRequest, Order.class);

        //since springBootTest is running on random port, can't hard code the path
        ResponseEntity<Order> response = restTemplate.getForEntity("/orders/{id}", Order.class, 1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        //the first primary key is 1
        Assertions.assertEquals(1, response.getBody().getOrderId());
    }
}
