package com.deye.userService.intergrationTest;

import com.deye.userService.domain.LoginRequest;
import com.deye.userService.domain.dto.UserDto;
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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@Testcontainers
@ActiveProfiles("test")
public class UserServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private Environment environment;

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void createUserIntegrationTest(){
        UserDto userDto = new UserDto();
        userDto.setUserName("user1");
        userDto.setEmail("email@gmail.com");
        userDto.setUserPassword("123456Abc@");

        ResponseEntity<UserDto> response = restTemplate.postForEntity("/user/register",userDto,UserDto.class);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(userDto.getUserName(),response.getBody().getUserName());
        Assertions.assertEquals(userDto.getEmail(),response.getBody().getEmail());
    }

    @Test
    void loginIntegrationTest(){
        UserDto userDto = new UserDto();
        userDto.setUserName("user1");
        userDto.setUserPassword("123Abc@");
        userDto.setEmail("user1@gmail.com");

        restTemplate.postForEntity("/user/register",userDto, UserDto.class);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(userDto.getEmail());
        loginRequest.setPassword(userDto.getUserPassword());

        ResponseEntity<String> response = restTemplate.postForEntity("/user/auth/login",loginRequest,String.class);

        Assertions.assertNotNull(response.getBody());

        System.out.println(response.getBody());
    }
}
