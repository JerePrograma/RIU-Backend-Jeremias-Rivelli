package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.application.exception.SearchNotFoundException;
import com.riu.hotelsearch.domain.port.out.FindSearchPort;
import com.riu.hotelsearch.domain.port.out.GetSearchCountPort;
import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.model.SearchCount;
import com.riu.hotelsearch.domain.model.SearchRecord;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountSearchServiceTest {

    private final FindSearchPort findSearchPort = mock(FindSearchPort.class);
    private final GetSearchCountPort getSearchCountPort = mock(GetSearchCountPort.class);

    private final CountSearchService service = new CountSearchService(findSearchPort, getSearchCountPort);

    @Test
    void shouldReturnSearchAndCountWhenRecordExists() {
        Search search = new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );
        SearchRecord record = new SearchRecord(
                "search-id-1",
                search,
                "fingerprint-1",
                Instant.parse("2026-03-31T18:10:00Z")
        );

        when(findSearchPort.findById("search-id-1")).thenReturn(Optional.of(record));
        when(getSearchCountPort.getByFingerprint("fingerprint-1")).thenReturn(100L);

        SearchCount result = service.count("search-id-1");

        assertEquals("search-id-1", result.searchId());
        assertEquals(search, result.search());
        assertEquals(100L, result.count());
    }

    @Test
    void shouldThrowWhenSearchDoesNotExist() {
        when(findSearchPort.findById("missing-id")).thenReturn(Optional.empty());

        SearchNotFoundException ex = assertThrows(SearchNotFoundException.class, () -> service.count("missing-id"));

        assertEquals("Search not found: missing-id", ex.getMessage());
        verifyNoInteractions(getSearchCountPort);
    }
}
