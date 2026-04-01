package com.riu.hotelsearch.adapter.out.kafka;

import com.riu.hotelsearch.application.port.in.PersistSearchUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.Acknowledgment;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class KafkaSearchConsumerTest {

    private final PersistSearchUseCase persistSearchUseCase = mock(PersistSearchUseCase.class);
    private final Acknowledgment acknowledgment = mock(Acknowledgment.class);

    private final KafkaSearchConsumer consumer = new KafkaSearchConsumer(persistSearchUseCase);

    @Test
    void shouldPersistAndAcknowledgeOnSuccess() {
        SearchMessage message = new SearchMessage(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        consumer.consume(message, acknowledgment);

        verify(persistSearchUseCase).persist(message);
        verify(acknowledgment).acknowledge();
    }

    @Test
    void shouldNotAcknowledgeWhenPersistenceFails() {
        SearchMessage message = new SearchMessage(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        doThrow(new RuntimeException("boom")).when(persistSearchUseCase).persist(message);

        assertThrows(RuntimeException.class, () -> consumer.consume(message, acknowledgment));

        verify(persistSearchUseCase).persist(message);
        verify(acknowledgment, never()).acknowledge();
    }
}
