package com.riu.hotelsearch.infrastructure.out.messaging.kafka;

import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;
import com.riu.hotelsearch.infrastructure.config.kafka.AppKafkaProperties;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        SearchRegisteredEvent event = new SearchRegisteredEvent(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        producer.publish(event);

        ArgumentCaptor<SearchMessage> captor = ArgumentCaptor.forClass(SearchMessage.class);

        verify(kafkaTemplate).send(eq("hotel_availability_searches"), eq("fingerprint-1"), captor.capture());

        SearchMessage message = captor.getValue();
        assertEquals("search-id-1", message.searchId());
        assertEquals("fingerprint-1", message.fingerprint());
        assertEquals("1234aBc", message.hotelId());
        assertEquals(LocalDate.of(2023, 12, 29), message.checkIn());
        assertEquals(LocalDate.of(2023, 12, 31), message.checkOut());
        assertEquals(List.of(30, 29, 1, 3), message.ages());
    }
}