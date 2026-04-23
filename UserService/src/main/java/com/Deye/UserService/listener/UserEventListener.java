package com.Deye.UserService.listener;

import com.Deye.UserService.event.OrderCreatedEvent;
import com.Deye.UserService.producer.UserEventProducer;
import com.Deye.UserService.service.UserService;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {
    private UserService userService;
    private UserEventProducer userEventProducer;
    private Logger logger;

    public UserEventListener(UserService userService, UserEventProducer userEventProducer) {
        this.userService = userService;
        this.userEventProducer = userEventProducer;
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
        return;
    }
}
