package com.riu.hotelsearch.infrastructure.out.messaging.kafka;

import com.riu.hotelsearch.application.port.in.PersistSearchUseCase;
import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;
import com.riu.hotelsearch.infrastructure.in.messaging.kafka.KafkaSearchConsumer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.support.Acknowledgment;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        ArgumentCaptor<SearchRegisteredEvent> captor =
                ArgumentCaptor.forClass(SearchRegisteredEvent.class);

        verify(persistSearchUseCase).persist(captor.capture());
        verify(acknowledgment).acknowledge();

        SearchRegisteredEvent event = captor.getValue();
        assertEquals("search-id-1", event.searchId());
        assertEquals("fingerprint-1", event.fingerprint());
        assertEquals("1234aBc", event.hotelId());
        assertEquals(LocalDate.of(2023, 12, 29), event.checkIn());
        assertEquals(LocalDate.of(2023, 12, 31), event.checkOut());
        assertEquals(List.of(30, 29, 1, 3), event.ages());
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

        doThrow(new RuntimeException("boom"))
                .when(persistSearchUseCase)
                .persist(any(SearchRegisteredEvent.class));

        assertThrows(RuntimeException.class, () -> consumer.consume(message, acknowledgment));

        verify(persistSearchUseCase).persist(any(SearchRegisteredEvent.class));
        verify(acknowledgment, never()).acknowledge();
    }
}