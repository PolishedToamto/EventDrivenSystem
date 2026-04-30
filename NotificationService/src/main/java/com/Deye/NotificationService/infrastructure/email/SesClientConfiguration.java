package com.Deye.NotificationService.infrastructure.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class SesClientConfiguration {
    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create("default"))
                .build();
    }
}
