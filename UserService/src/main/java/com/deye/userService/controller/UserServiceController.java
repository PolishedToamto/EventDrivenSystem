package com.deye.userService.controller;

import com.deye.userService.domain.LoginRequest;
import com.deye.userService.domain.LoginResponse;
import com.deye.userService.domain.dto.UserDto;
import com.deye.userService.domain.entity.User;
import com.deye.userService.domain.mapper.UserMapper;
import com.deye.userService.repository.UserRepository;
import com.deye.userService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserServiceController {

    private final Logger logger = LoggerFactory.getLogger(UserServiceController.class);
    private static final String CLASS_NAME = UserServiceController.class.getName();
    private static final String ENTER_MESSAGE = "Entering " + CLASS_NAME;
    private static final String EXIT_MESSAGE = "Exiting " + CLASS_NAME;

    private final UserService userService;

    public UserServiceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id){
        String methodName = ".getUserById()";
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        logger.info(ENTER_MESSAGE + methodName);

        logger.info(EXIT_MESSAGE + methodName);
        return userService.getUserNameById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user){
        String methodName = ".createUser()";
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        logger.info(ENTER_MESSAGE + methodName);

        UserDto dto = userService.createUser(user);


        logger.info(EXIT_MESSAGE + methodName);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        String methodName = ".login()";
        logger.info("{}{}", ENTER_MESSAGE, methodName);
        MDC.put("correlationId", UUID.randomUUID().toString());

        LoginResponse response = userService.login(request);

        logger.info(EXIT_MESSAGE + methodName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
