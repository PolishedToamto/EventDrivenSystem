package com.Deye.NotificationService.infrastructure.email;

import com.Deye.NotificationService.application.service.EmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Component
public class AwsSesSmtpEmailSender implements EmailSender {

    private final SesClient sesClient;
    private final String from;

    public AwsSesSmtpEmailSender(SesClient sesClient,
                                 @Value("${email.from}") String from
                                 ) {
        this.sesClient = sesClient;
        this.from = from;
    }

    @Override
    public void send(String to, String subject, String body) {
        SendEmailRequest request = SendEmailRequest.builder()
                .destination(Destination.builder()
                        .toAddresses(to)
                        .build())
                .message(Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder()
                                .text(Content.builder().data(body).build())
                                .build())
                        .build())
                .source(from)
                .build();

        sesClient.sendEmail(request);
    }
}
