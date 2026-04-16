package com.Deye.NotificationService.unitTest;

import com.Deye.NotificationService.application.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private NotificationService notificationService;

    @Test
    public void testSendNotification() {
        notificationService.sendNotification(0, "hello world");

        verify(notificationService).sendNotification(eq(0), eq("hello world"));
    }
}
