package com.deye.userService.listener;

import com.deye.userService.event.OrderCreatedEvent;
import com.deye.userService.producer.UserEventProducer;
import com.deye.userService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {
    private final UserService userService;
    private final UserEventProducer userEventProducer;
    private final Logger logger;

    public UserEventListener(UserService userService, UserEventProducer userEventProducer) {
        this.userService = userService;
        this.userEventProducer = userEventProducer;
        this.logger = LoggerFactory.getLogger(UserEventListener.class);
    }

    @KafkaListener(topics = "order.event")
    public void handleOrderCreated(OrderCreatedEvent event) {
        logger.info("UserService received: " + event.getOrderId());

        if(!userService.isValidUser(event.getUserId())
            || !userService.isValidEmail(event.getEmail())){
            logger.info("user event is not valid");
            userEventProducer.sendUserEvent(event, false);
            return;
        }

        logger.info("user is valid: " + event.getUserId());

        userEventProducer.sendUserEvent(event, true);
    }
}
