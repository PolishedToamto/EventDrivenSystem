package com.Deye.NotificationService.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void sendNotification(Integer userId, String message) {
        logger.info("send notification");

        try{
            Thread.sleep(500);
        }
        catch(InterruptedException e){

        }
    }
}
