package com.riu.hotelsearch.adapter.in.rest.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchRequestDtoTest {

    @Test
    void shouldDefensivelyCopyAges() {
        List<Integer> ages = new ArrayList<>(List.of(30, 29, 1, 3));

        SearchRequestDto dto = new SearchRequestDto(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                ages
        );

        ages.add(99);

        assertEquals(List.of(30, 29, 1, 3), dto.ages());
        assertThrows(UnsupportedOperationException.class, () -> dto.ages().add(77));
    }

    @Test
    void shouldKeepNullAgesAsNull() {
        SearchRequestDto dto = new SearchRequestDto(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                null
        );

        assertNull(dto.ages());
    }
}
