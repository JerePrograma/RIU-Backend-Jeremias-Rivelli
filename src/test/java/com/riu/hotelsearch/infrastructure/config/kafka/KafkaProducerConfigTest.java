package com.riu.hotelsearch.infrastructure.config.kafka;

import com.riu.hotelsearch.domain.port.out.PublishSearchEventPort;
import com.riu.hotelsearch.infrastructure.out.messaging.kafka.KafkaSearchProducer;
import com.riu.hotelsearch.infrastructure.out.messaging.kafka.SearchMessage;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

class KafkaProducerConfigTest {

    private final KafkaProducerConfig config = new KafkaProducerConfig();

    @Test
    void shouldCreatePublishSearchEventPort() {
        @SuppressWarnings("unchecked")
        KafkaTemplate<String, SearchMessage> kafkaTemplate = mock(KafkaTemplate.class);

        AppKafkaProperties properties = new AppKafkaProperties(
                "hotel_availability_searches",
                4,
                (short) 1,
                4
        );

        PublishSearchEventPort bean = config.publishSearchEventPort(kafkaTemplate, properties);

        assertInstanceOf(KafkaSearchProducer.class, bean);
    }
}