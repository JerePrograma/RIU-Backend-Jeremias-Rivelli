package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;
import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.port.out.PublishSearchEventPort;
import com.riu.hotelsearch.domain.port.out.SearchIdGenerator;
import com.riu.hotelsearch.domain.service.SearchFingerprintCalculator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RegisterSearchServiceTest {

    private final PublishSearchEventPort publishSearchEventPort = mock(PublishSearchEventPort.class);
    private final SearchIdGenerator searchIdGenerator = mock(SearchIdGenerator.class);
    private final SearchFingerprintCalculator fingerprintCalculator = mock(SearchFingerprintCalculator.class);

    private final RegisterSearchService service = new RegisterSearchService(
            publishSearchEventPort,
            searchIdGenerator,
            fingerprintCalculator
    );

    @Test
    void shouldGenerateIdCalculateFingerprintAndPublishEvent() {
        Search search = new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        when(searchIdGenerator.nextId()).thenReturn("search-id-1");
        when(fingerprintCalculator.calculate(search)).thenReturn("fingerprint-1");

        String result = service.register(search);

        assertEquals("search-id-1", result);

        ArgumentCaptor<SearchRegisteredEvent> captor =
                ArgumentCaptor.forClass(SearchRegisteredEvent.class);

        verify(publishSearchEventPort).publish(captor.capture());

        SearchRegisteredEvent event = captor.getValue();
        assertEquals("search-id-1", event.searchId());
        assertEquals("fingerprint-1", event.fingerprint());
        assertEquals("1234aBc", event.hotelId());
        assertEquals(LocalDate.of(2023, 12, 29), event.checkIn());
        assertEquals(LocalDate.of(2023, 12, 31), event.checkOut());
        assertEquals(List.of(30, 29, 1, 3), event.ages());
    }

    @Test
    void shouldPreserveAgeOrderInPublishedEvent() {
        Search search = new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(3, 29, 30, 1)
        );

        when(searchIdGenerator.nextId()).thenReturn("search-id-2");
        when(fingerprintCalculator.calculate(search)).thenReturn("fingerprint-2");

        service.register(search);

        ArgumentCaptor<SearchRegisteredEvent> captor =
                ArgumentCaptor.forClass(SearchRegisteredEvent.class);

        verify(publishSearchEventPort).publish(captor.capture());

        assertEquals(List.of(3, 29, 30, 1), captor.getValue().ages());
    }
}