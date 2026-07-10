package com.Deye.OrderService.integrationTest;

import com.Deye.OrderService.config.SecurityConfig;
import com.Deye.OrderService.dto.OrderRequest;
import com.Deye.OrderService.entity.Order;
import com.Deye.OrderService.entity.User;
import com.Deye.OrderService.event.OrderCreatedEvent;
import com.Deye.OrderService.repository.OrderRepository;
import com.Deye.OrderService.repository.UserRepository;
import com.Deye.OrderService.secruity.JwtAuthenticationFilter;
import com.Deye.OrderService.service.CustomUserDetailService;
import com.Deye.OrderService.service.JwtService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    JwtService jwtService;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    SecurityConfig securityConfig;

    @Autowired
    UserRepository userRepository;

    @Autowired
    Environment env;

    @Test
    void debug() {
        //testing if application-test yml is loaded
        System.out.println(env.getProperty("test.marker"));
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        // priority from hight to low
        // dynamicPropertySource -> application-test-yml -> application.yml
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void unAuthorizedRequestTest() {
        HttpEntity<Order> httpEntity = new HttpEntity<>(new Order());
        httpEntity.getHeaders().add(HttpHeaders.AUTHORIZATION, "123");

        ResponseEntity<Order> response = restTemplate.postForEntity("/orders", httpEntity, Order.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);

        HttpEntity<String> httpEntity1 = new HttpEntity<>(new String());
        httpEntity1.getHeaders().add(HttpHeaders.AUTHORIZATION, "123");

        ResponseEntity<String> response1 = restTemplate.postForEntity("/orders/{id}", httpEntity1, String.class, 1);
        Assertions.assertEquals(response1.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void createOrderIntegrationTest() throws InterruptedException {
        User user = new User();

        user.setId(99);
        user.setUserName("test");
        user.setEmail("test@gmail.com");
        user.setCreatedAt(LocalDate.now());
        user.setPasswordHash("123");

        String token = jwtService.generateAccessToken(user);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(0);
        orderRequest.setProductName("testProduct");
        orderRequest.setQuantity(1);
        orderRequest.setPrice(new BigDecimal("15.99"));
        orderRequest.setEmail("test@gmail.com");

        HttpEntity<OrderRequest> httpEntity = new HttpEntity<>(orderRequest);
        httpEntity.getHeaders().add(HttpHeaders.AUTHORIZATION, token);

        ResponseEntity<com.Deye.OrderService.entity.Order> response = restTemplate.postForEntity("/orders", httpEntity, Order.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        com.Deye.OrderService.entity.Order saved = orderRepository.findById(response.getBody().getOrderId()).orElse(null);

        assertNotNull(saved);

        assertEquals(response.getBody().getUserId(), saved.getUserId());
        assertEquals("testProduct", saved.getProductName());
        assertEquals(1, saved.getQuantity());
        assertTrue(new BigDecimal("15.99").compareTo(saved.getPrice()) == 0);
        assertEquals(response.getBody().getEmail(), saved.getEmail());

        ConsumerRecord<String, OrderCreatedEvent> orderCreatedEvent = kafkaListener.waitForMessage();
        assertNotNull(orderCreatedEvent);

        assertEquals("testProduct", orderCreatedEvent.value().getProductName());

        assertNotNull(orderCreatedEvent.headers().lastHeader("X-Correlation-Id"));
    }

    @Test
    public void getOrderByIdIntegrationTest(){
        User user = new User();

        user.setId(99);
        user.setUserName("test");
        user.setEmail("test@gmail.com");
        user.setCreatedAt(LocalDate.now());
        user.setPasswordHash("123");

        String token = jwtService.generateAccessToken(user);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(0);
        orderRequest.setProductName("testProduct");
        orderRequest.setQuantity(1);
        orderRequest.setPrice(new BigDecimal("15.99"));
        orderRequest.setEmail("test@gmail.com");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<OrderRequest> httpEntity = new HttpEntity<>(orderRequest, httpHeaders);

        ResponseEntity<Order> response1 = restTemplate.postForEntity("/orders", httpEntity, Order.class);

        //since springBootTest is running on random port, can't hard code the path
        ResponseEntity<Order> response = restTemplate.exchange("/orders/{id}",  HttpMethod.GET, httpEntity, Order.class, 1);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        //the first primary key is 1
        Assertions.assertEquals(1, response.getBody().getOrderId());
    }
}
