package com.riu.hotelsearch.infrastructure.config.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de configuración del tópico Kafka y del listener asociado.
 */
@ConfigurationProperties(prefix = "app.kafka")
public record AppKafkaProperties(
        String topic,
        int partitions,
        short replicationFactor,
        int listenerConcurrency
) {
}