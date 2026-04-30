package com.deye.userService.listener;

import com.deye.userService.event.OrderCreatedEvent;
import com.deye.userService.producer.UserEventProducer;
import com.deye.userService.service.UserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserEventListener {
    private final UserService userService;
    private final UserEventProducer userEventProducer;
    private final Logger logger = LoggerFactory.getLogger(UserEventListener.class);

    public UserEventListener(UserService userService, UserEventProducer userEventProducer) {
        this.userService = userService;
        this.userEventProducer = userEventProducer;
    }

    @KafkaListener(topics = "order.event")
    public void handleOrderCreated(ConsumerRecord<String, OrderCreatedEvent> record) {
        OrderCreatedEvent event = record.value();
        String correlationId = new String(
                record.headers()
                        .lastHeader("X-Correlation-Id")//retrieve the last header with this key
                        .value()
        );

        MDC.put("correlationId", correlationId);

        logger.info("Entering UserEventListener.handleOrderCreated()");
        logger.info("OrderCreatedEvent received{}" + event);
        logger.info("correlation id " + correlationId);

        if(!userService.isValidUser(event.getUserId())
            || !userService.isValidEmail(event.getEmail())){
            logger.info("user event is not valid");
            userEventProducer.sendUserEvent(event, false);
            return;
        }

        logger.info("user event is valid");

        userEventProducer.sendUserEvent(event, true);

        logger.info("Exiting UserEventListener.handleOrderCreated()");

        MDC.clear();
    }
}
