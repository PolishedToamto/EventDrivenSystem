package com.Deye.NotificationService.unitTest;

import com.Deye.NotificationService.domain.event.UserValidatedEvent;
import com.Deye.NotificationService.infrastructure.kafka.listener.NotificationEventListener;
import com.Deye.NotificationService.application.service.EmailService;
import com.Deye.NotificationService.application.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationEventListenerTest {
    @Mock
    NotificationService notificationService;

    @Mock
    EmailService emailService;

    @InjectMocks
    NotificationEventListener notificationEventListener;

    @Test
    public void testOnEventListening() {
        UserValidatedEvent userValidatedEvent = new UserValidatedEvent(0, 0, true);

        notificationEventListener.sendNotification(userValidatedEvent);

        verify(emailService).sendEmail(
                eq("dleipersonal@gmail.com"),
                eq("Test Email"),
                eq("Hello this is a test email!")
        );

        verify(notificationService).sendNotification(eq(userValidatedEvent.getUserId()), eq("hello world"));
    }
}
