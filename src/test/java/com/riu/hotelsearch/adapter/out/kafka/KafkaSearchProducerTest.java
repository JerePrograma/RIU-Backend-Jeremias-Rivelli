package com.riu.hotelsearch.adapter.out.kafka;

import com.riu.hotelsearch.config.kafka.AppKafkaProperties;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

class KafkaSearchProducerTest {

    @SuppressWarnings("unchecked")
    private final KafkaTemplate<String, SearchMessage> kafkaTemplate = mock(KafkaTemplate.class);
    private final AppKafkaProperties properties = new AppKafkaProperties(
            "hotel_availability_searches",
            3,
            (short) 1,
            2
    );

    private final KafkaSearchProducer producer = new KafkaSearchProducer(kafkaTemplate, properties);

    @Test
    void shouldSendMessageUsingFingerprintAsKey() {
        SearchMessage message = new SearchMessage(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        producer.publish(message);

        verify(kafkaTemplate).send("hotel_availability_searches", "fingerprint-1", message);
    }
}
