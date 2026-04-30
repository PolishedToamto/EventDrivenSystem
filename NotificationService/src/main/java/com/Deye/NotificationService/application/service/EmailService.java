package com.Deye.NotificationService.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final EmailSender emailSender;
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        logger.info("Entering EmaiLService.sendEmail()");
        emailSender.send(to, subject, body);
        logger.info("Exiting EmaiLService.sendEmail()");
    }
}
