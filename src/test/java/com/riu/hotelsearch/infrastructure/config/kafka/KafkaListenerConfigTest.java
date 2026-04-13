package com.riu.hotelsearch.infrastructure.config.kafka;

import com.riu.hotelsearch.infrastructure.out.messaging.kafka.SearchMessage;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class KafkaListenerConfigTest {

    private final KafkaListenerConfig config = new KafkaListenerConfig();

    @Test
    void shouldConfigureKafkaListenerContainerFactory() {
        @SuppressWarnings("unchecked")
        ConsumerFactory<String, SearchMessage> consumerFactory = mock(ConsumerFactory.class);

        AsyncTaskExecutor kafkaListenerTaskExecutor = mock(AsyncTaskExecutor.class);

        AppKafkaProperties properties = new AppKafkaProperties(
                "hotel_availability_searches",
                1,
                (short) 1,
                3
        );

        ConcurrentKafkaListenerContainerFactory<String, SearchMessage> factory =
                config.searchKafkaListenerContainerFactory(
                        consumerFactory,
                        properties,
                        kafkaListenerTaskExecutor
                );

        assertThat(factory).isNotNull();
        assertThat(factory.getConsumerFactory()).isSameAs(consumerFactory);
        assertThat(factory.getContainerProperties().getAckMode())
                .isEqualTo(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        assertThat(factory.getContainerProperties().isMissingTopicsFatal()).isTrue();
    }
}