package com.Deye.NotificationService.service;

import com.Deye.NotificationService.event.OrderCreatedEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendNotification(Integer userId, String message) {
        try{
            Thread.sleep(500);
        }
        catch(InterruptedException e){

        }
    }
}
