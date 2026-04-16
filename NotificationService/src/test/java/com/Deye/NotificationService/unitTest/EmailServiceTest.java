package com.Deye.NotificationService.unitTest;

import com.Deye.NotificationService.application.service.EmailSender;
import com.Deye.NotificationService.application.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendEmail() {
        emailService.sendEmail("a","test","body");

        (verify(emailSender)).send(eq("a"),eq("test"),eq("body"));

    }
}
