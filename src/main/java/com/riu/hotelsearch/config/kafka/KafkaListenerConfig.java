package com.riu.hotelsearch.config.kafka;

import com.riu.hotelsearch.adapter.out.kafka.SearchMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class KafkaListenerConfig {

    /**
     * Configura el contenedor de listeners Kafka utilizado por la aplicación.
     *
     * <p>La confirmación manual del offset permite reconocer el mensaje solo
     * después de completar su persistencia.</p>
     */
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