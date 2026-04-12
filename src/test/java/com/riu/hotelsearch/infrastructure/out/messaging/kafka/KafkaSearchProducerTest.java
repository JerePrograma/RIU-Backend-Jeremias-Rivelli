package com.riu.hotelsearch.infrastructure.out.messaging.kafka;

import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;
import com.riu.hotelsearch.infrastructure.config.kafka.AppKafkaProperties;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
    void shouldSendMessageUsingFingerprintAsKey() throws Exception {
        SearchRegisteredEvent event = new SearchRegisteredEvent(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, SearchMessage>> future = mock(CompletableFuture.class);
        when(kafkaTemplate.send(eq("hotel_availability_searches"), eq("fingerprint-1"), any(SearchMessage.class)))
                .thenReturn(future);
        when(future.get(5, TimeUnit.SECONDS)).thenReturn(null);

        producer.publish(event);

        verify(kafkaTemplate).send(eq("hotel_availability_searches"), eq("fingerprint-1"), any(SearchMessage.class));
        verify(future).get(5, TimeUnit.SECONDS);
    }
}