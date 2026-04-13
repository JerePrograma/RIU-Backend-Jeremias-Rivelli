package com.riu.hotelsearch.infrastructure.config.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.core.task.AsyncTaskExecutor;

import static org.assertj.core.api.Assertions.assertThat;

class VirtualThreadsConfigTest {

    private final VirtualThreadsConfig config = new VirtualThreadsConfig();

    @Test
    void shouldCreateKafkaListenerTaskExecutor() {
        AppKafkaProperties properties = new AppKafkaProperties(
                "hotel_availability_searches",
                4,
                (short) 1,
                3
        );

        AsyncTaskExecutor executor = config.kafkaListenerTaskExecutor(properties);

        assertThat(executor).isNotNull();
    }
}