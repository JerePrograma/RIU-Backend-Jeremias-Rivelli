package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;
import com.riu.hotelsearch.domain.model.SearchRecord;
import com.riu.hotelsearch.domain.port.out.IncrementSearchCountPort;
import com.riu.hotelsearch.domain.port.out.SaveSearchIfAbsentPort;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersistSearchServiceTest {

    private final SaveSearchIfAbsentPort saveSearchIfAbsentPort = mock(SaveSearchIfAbsentPort.class);
    private final IncrementSearchCountPort incrementSearchCountPort = mock(IncrementSearchCountPort.class);

    private final PersistSearchService service = new PersistSearchService(
            saveSearchIfAbsentPort,
            incrementSearchCountPort
    );

    @Test
    void shouldPersistAndIncrementWhenInserted() {
        SearchRegisteredEvent event = new SearchRegisteredEvent(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        when(saveSearchIfAbsentPort.saveIfAbsent(any(SearchRecord.class))).thenReturn(true);

        service.persist(event);

        ArgumentCaptor<SearchRecord> captor = ArgumentCaptor.forClass(SearchRecord.class);

        verify(saveSearchIfAbsentPort).saveIfAbsent(captor.capture());
        verify(incrementSearchCountPort).increment("fingerprint-1");

        SearchRecord record = captor.getValue();
        assertEquals("search-id-1", record.searchId());
        assertEquals("fingerprint-1", record.fingerprint());
        assertEquals("1234aBc", record.search().hotelId());
        assertEquals(LocalDate.of(2023, 12, 29), record.search().checkIn());
        assertEquals(LocalDate.of(2023, 12, 31), record.search().checkOut());
        assertEquals(List.of(30, 29, 1, 3), record.search().ages());
        assertNotNull(record.createdAt());
    }

    @Test
    void shouldNotIncrementWhenSearchAlreadyExists() {
        SearchRegisteredEvent event = new SearchRegisteredEvent(
                "search-id-2",
                "fingerprint-2",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        when(saveSearchIfAbsentPort.saveIfAbsent(any(SearchRecord.class))).thenReturn(false);

        service.persist(event);

        verify(saveSearchIfAbsentPort).saveIfAbsent(any(SearchRecord.class));
        verifyNoInteractions(incrementSearchCountPort);
    }
}