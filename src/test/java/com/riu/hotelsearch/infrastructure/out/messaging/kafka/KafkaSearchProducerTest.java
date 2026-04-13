package com.riu.hotelsearch.infrastructure.out.messaging.kafka;

import com.riu.hotelsearch.application.exception.SearchPublicationException;
import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;
import com.riu.hotelsearch.infrastructure.config.kafka.AppKafkaProperties;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class KafkaSearchProducerTest {

    @SuppressWarnings("unchecked")
    private final KafkaTemplate<String, SearchMessage> kafkaTemplate = mock(KafkaTemplate.class);

    private final AppKafkaProperties properties = new AppKafkaProperties(
            "hotel_availability_searches",
            4,
            (short) 1,
            4
    );

    private final KafkaSearchProducer producer = new KafkaSearchProducer(kafkaTemplate, properties);

    @Test
    void shouldPublishSuccessfully() {
        CompletableFuture<SendResult<String, SearchMessage>> future = CompletableFuture.completedFuture(null);

        when(kafkaTemplate.send(anyString(), anyString(), any(SearchMessage.class))).thenReturn(future);

        producer.publish(validEvent());

        verify(kafkaTemplate).send(
                eq("hotel_availability_searches"),
                eq("fp-1"),
                any(SearchMessage.class)
        );
    }

    @Test
    void shouldWrapInterruptedExceptionAndRestoreThreadInterruptFlag() throws Exception {
        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, SearchMessage>> future = mock(CompletableFuture.class);

        when(kafkaTemplate.send(anyString(), anyString(), any(SearchMessage.class))).thenReturn(future);
        when(future.get(eq(5L), any())).thenThrow(new InterruptedException("boom"));

        SearchPublicationException ex = assertThrows(
                SearchPublicationException.class,
                () -> producer.publish(validEvent())
        );

        assertEquals("Interrupted while publishing search event", ex.getMessage());
        assertInstanceOf(InterruptedException.class, ex.getCause());
        assertTrue(Thread.currentThread().isInterrupted());

        Thread.interrupted();
    }

    @Test
    void shouldWrapExecutionException() throws Exception {
        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, SearchMessage>> future = mock(CompletableFuture.class);

        when(kafkaTemplate.send(anyString(), anyString(), any(SearchMessage.class))).thenReturn(future);
        when(future.get(eq(5L), any())).thenThrow(new ExecutionException(new RuntimeException("boom")));

        SearchPublicationException ex = assertThrows(
                SearchPublicationException.class,
                () -> producer.publish(validEvent())
        );

        assertEquals("Could not publish search event to Kafka", ex.getMessage());
        assertInstanceOf(ExecutionException.class, ex.getCause());
    }

    @Test
    void shouldWrapTimeoutException() throws Exception {
        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, SearchMessage>> future = mock(CompletableFuture.class);

        when(kafkaTemplate.send(anyString(), anyString(), any(SearchMessage.class))).thenReturn(future);
        when(future.get(eq(5L), any())).thenThrow(new TimeoutException("boom"));

        SearchPublicationException ex = assertThrows(
                SearchPublicationException.class,
                () -> producer.publish(validEvent())
        );

        assertEquals("Could not publish search event to Kafka", ex.getMessage());
        assertInstanceOf(TimeoutException.class, ex.getCause());
    }

    private SearchRegisteredEvent validEvent() {
        return new SearchRegisteredEvent(
                "id-1",
                "fp-1",
                "hotel-1",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );
    }
}