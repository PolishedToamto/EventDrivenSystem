package com.Deye.NotificationService.application.service;

public interface EmailSender {
    void send(String to, String subject, String body);
}
