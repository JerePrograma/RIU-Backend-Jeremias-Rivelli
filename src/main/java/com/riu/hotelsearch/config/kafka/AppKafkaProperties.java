package com.riu.hotelsearch.config.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record AppKafkaProperties(
        String topic,
        int partitions,
        short replicationFactor,
        int listenerConcurrency
) {
}