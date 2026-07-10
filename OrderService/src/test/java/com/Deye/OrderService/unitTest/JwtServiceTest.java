package com.Deye.OrderService.unitTest;

import com.Deye.OrderService.entity.User;
import com.Deye.OrderService.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("L2dG4Y7mC6xN9qR8tK3pV5sA1bE0uWzFjHnQ4rY6mD8cP2xT", 3600);
    }

    @Test
    void extractEmail() {
        User user = new User();
        user.setId(99);
        user.setUserName("user1");
        user.setEmail("email1@gmail.com");
        user.setPasswordHash("asdsdfa");
        user.setCreatedAt(LocalDate.now());

        String token = jwtService.generateAccessToken(user);

        String extractedEmail = jwtService.extractEmail(token);

        Assertions.assertEquals(user.getEmail(), extractedEmail);
    }

    @Test
    void isTokenValid() {
        User correctUser = new User();
        correctUser.setId(99);
        correctUser.setUserName("user1");
        correctUser.setEmail("email1@gmail.com");
        correctUser.setPasswordHash("asdsdfa");
        correctUser.setCreatedAt(LocalDate.now());

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(correctUser.getEmail())
                .authorities("ROLE_USER")
                .build();

        User wrongUser = new User();
        wrongUser.setId(99);
        wrongUser.setUserName("user2");
        wrongUser.setEmail("email2@gmail.com");
        wrongUser.setPasswordHash("asdsdfa");
        wrongUser.setCreatedAt(LocalDate.now());

        String correctToken = jwtService.generateAccessToken(correctUser);
        String wrongToken = jwtService.generateAccessToken(wrongUser);

        boolean shouldBeTrue = jwtService.isTokenValid(correctToken, userDetails);
        boolean shouldBeFalse = jwtService.isTokenValid(wrongToken, userDetails);

        Assertions.assertTrue(shouldBeTrue);
        Assertions.assertFalse(shouldBeFalse);
    }
}