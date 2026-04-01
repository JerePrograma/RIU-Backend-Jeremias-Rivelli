package com.riu.hotelsearch.adapter.out.persistence;

import com.riu.hotelsearch.domain.model.SearchRecord;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchRowMapperTest {

    private final SearchRowMapper mapper = new SearchRowMapper();

    @Test
    void shouldMapRowToSearchRecord() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Instant createdAt = Instant.parse("2026-03-31T18:10:00Z");

        when(rs.getString("search_id")).thenReturn("search-id-1");
        when(rs.getString("hotel_id")).thenReturn("1234aBc");
        when(rs.getDate("check_in")).thenReturn(java.sql.Date.valueOf(LocalDate.of(2023, 12, 29)));
        when(rs.getDate("check_out")).thenReturn(java.sql.Date.valueOf(LocalDate.of(2023, 12, 31)));
        when(rs.getString("ages_csv")).thenReturn("30,29,1,3");
        when(rs.getString("fingerprint")).thenReturn("fingerprint-1");
        when(rs.getTimestamp("created_at")).thenReturn(Timestamp.from(createdAt));

        SearchRecord result = mapper.mapRow(rs, 1);

        assertEquals("search-id-1", result.searchId());
        assertEquals("1234aBc", result.search().hotelId());
        assertEquals(LocalDate.of(2023, 12, 29), result.search().checkIn());
        assertEquals(LocalDate.of(2023, 12, 31), result.search().checkOut());
        assertEquals(List.of(30, 29, 1, 3), result.search().ages());
        assertEquals("fingerprint-1", result.fingerprint());
        assertEquals(createdAt, result.createdAt());
    }

    @Test
    void shouldFailWhenCsvIsBlankBecauseDomainSearchRejectsEmptyAges() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Instant createdAt = Instant.parse("2026-03-31T18:10:00Z");

        when(rs.getString("search_id")).thenReturn("search-id-2");
        when(rs.getString("hotel_id")).thenReturn("1234aBc");
        when(rs.getDate("check_in")).thenReturn(java.sql.Date.valueOf(LocalDate.of(2023, 12, 29)));
        when(rs.getDate("check_out")).thenReturn(java.sql.Date.valueOf(LocalDate.of(2023, 12, 31)));
        when(rs.getString("ages_csv")).thenReturn("   ");
        when(rs.getString("fingerprint")).thenReturn("fingerprint-2");
        when(rs.getTimestamp("created_at")).thenReturn(Timestamp.from(createdAt));

        assertThrows(RuntimeException.class, () -> mapper.mapRow(rs, 1));
    }

    @Test
    void shouldFailWhenCsvIsNullBecauseDomainSearchRejectsEmptyAges() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Instant createdAt = Instant.parse("2026-03-31T18:10:00Z");

        when(rs.getString("search_id")).thenReturn("search-id-3");
        when(rs.getString("hotel_id")).thenReturn("1234aBc");
        when(rs.getDate("check_in")).thenReturn(java.sql.Date.valueOf(LocalDate.of(2023, 12, 29)));
        when(rs.getDate("check_out")).thenReturn(java.sql.Date.valueOf(LocalDate.of(2023, 12, 31)));
        when(rs.getString("ages_csv")).thenReturn(null);
        when(rs.getString("fingerprint")).thenReturn("fingerprint-3");
        when(rs.getTimestamp("created_at")).thenReturn(Timestamp.from(createdAt));

        assertThrows(RuntimeException.class, () -> mapper.mapRow(rs, 1));
    }

    @Test
    void shouldConvertAgesToCsvPreservingOrder() {
        String agesCsv = mapper.toAgesCsv(List.of(3, 29, 30, 1));

        assertEquals("3,29,30,1", agesCsv);
    }
}
