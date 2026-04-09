package com.riu.hotelsearch.infrastructure.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    /**
     * Declara el tópico Kafka utilizado para registrar búsquedas de disponibilidad.
     */
    @Bean
    public NewTopic hotelAvailabilitySearchesTopic(AppKafkaProperties properties) {
        return new NewTopic(
                properties.topic(),
                properties.partitions(),
                properties.replicationFactor()
        );
    }
}