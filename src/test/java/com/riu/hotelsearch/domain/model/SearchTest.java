package com.riu.hotelsearch.domain.model;

import com.riu.hotelsearch.domain.exception.InvalidSearchException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    @Test
    void shouldCreateValidSearch() {
        Search search = new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        assertEquals("1234aBc", search.hotelId());
        assertEquals(LocalDate.of(2023, 12, 29), search.checkIn());
        assertEquals(LocalDate.of(2023, 12, 31), search.checkOut());
        assertEquals(List.of(30, 29, 1, 3), search.ages());
    }

    @Test
    void shouldFailWhenHotelIdIsNull() {
        InvalidSearchException ex = assertThrows(InvalidSearchException.class, () -> new Search(
                null,
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30)
        ));

        assertEquals("hotelId must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenHotelIdIsBlank() {
        InvalidSearchException ex = assertThrows(InvalidSearchException.class, () -> new Search(
                "   ",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30)
        ));

        assertEquals("hotelId must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenCheckInIsNull() {
        InvalidSearchException ex = assertThrows(InvalidSearchException.class, () -> new Search(
                "1234aBc",
                null,
                LocalDate.of(2023, 12, 31),
                List.of(30)
        ));

        assertEquals("checkIn must not be null", ex.getMessage());
    }

    @Test
    void shouldFailWhenCheckOutIsNull() {
        InvalidSearchException ex = assertThrows(InvalidSearchException.class, () -> new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                null,
                List.of(30)
        ));

        assertEquals("checkOut must not be null", ex.getMessage());
    }

    @Test
    void shouldFailWhenAgesIsNull() {
        InvalidSearchException ex = assertThrows(InvalidSearchException.class, () -> new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                null
        ));

        assertEquals("ages must not be null", ex.getMessage());
    }

    @Test
    void shouldFailWhenAgesIsEmpty() {
        InvalidSearchException ex = assertThrows(InvalidSearchException.class, () -> new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of()
        ));

        assertEquals("ages must not be empty", ex.getMessage());
    }

    @Test
    void shouldFailWhenAgesContainsNull() {
        List<Integer> ages = new ArrayList<>();
        ages.add(30);
        ages.add(null);
        ages.add(1);

        InvalidSearchException ex = assertThrows(InvalidSearchException.class, () -> new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                ages
        ));

        assertEquals("ages must not contain null values", ex.getMessage());
    }

    @Test
    void shouldFailWhenAgesContainsNegativeValue() {
        InvalidSearchException ex = assertThrows(InvalidSearchException.class, () -> new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, -1, 1)
        ));

        assertEquals("ages must contain non-negative values", ex.getMessage());
    }

    @Test
    void shouldFailWhenCheckInEqualsCheckOut() {
        InvalidSearchException ex = assertThrows(InvalidSearchException.class, () -> new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 29),
                List.of(30)
        ));

        assertEquals("checkIn must be before checkOut", ex.getMessage());
    }

    @Test
    void shouldFailWhenCheckInIsAfterCheckOut() {
        InvalidSearchException ex = assertThrows(InvalidSearchException.class, () -> new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2023, 12, 29),
                List.of(30)
        ));

        assertEquals("checkIn must be before checkOut", ex.getMessage());
    }

    @Test
    void shouldDefensivelyCopyAges() {
        List<Integer> sourceAges = new ArrayList<>(List.of(30, 29, 1, 3));

        Search search = new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                sourceAges
        );

        sourceAges.add(99);

        assertEquals(List.of(30, 29, 1, 3), search.ages());
        assertThrows(UnsupportedOperationException.class, () -> search.ages().add(88));
    }
}
