package com.riu.hotelsearch.config.kafka;

import com.riu.hotelsearch.adapter.out.kafka.SearchMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class KafkaListenerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SearchMessage> searchKafkaListenerContainerFactory(
            ConsumerFactory<String, SearchMessage> consumerFactory,
            AppKafkaProperties properties
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, SearchMessage>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(properties.listenerConcurrency());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setMissingTopicsFatal(true);
        return factory;
    }
}