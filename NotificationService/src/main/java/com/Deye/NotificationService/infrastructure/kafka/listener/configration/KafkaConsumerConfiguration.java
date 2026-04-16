package com.Deye.NotificationService.infrastructure.kafka.listener.configration;

import com.Deye.NotificationService.domain.event.UserValidatedEvent;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfiguration {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserValidatedEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, UserValidatedEvent> consumerFactory,
            KafkaTemplate<String, Object> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, UserValidatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(
                        new DeadLetterPublishingRecoverer(kafkaTemplate, (record, e) -> new TopicPartition("user.event.dlt", record.partition())),
                        new FixedBackOff(2000L, 3)
                );

        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}
