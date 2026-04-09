package com.riu.hotelsearch.infrastructure.out.messaging.kafka;

import com.riu.hotelsearch.infrastructure.out.messaging.kafka.SearchMessage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchMessageTest {

    @Test
    void shouldCreateValidMessage() {
        SearchMessage message = new SearchMessage(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        assertEquals("search-id-1", message.searchId());
        assertEquals("fingerprint-1", message.fingerprint());
        assertEquals("1234aBc", message.hotelId());
        assertEquals(LocalDate.of(2023, 12, 29), message.checkIn());
        assertEquals(LocalDate.of(2023, 12, 31), message.checkOut());
        assertEquals(List.of(30, 29, 1, 3), message.ages());
    }

    @Test
    void shouldFailWhenSearchIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SearchMessage(
                null,
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30)
        ));

        assertEquals("searchId must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenSearchIdIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SearchMessage(
                "   ",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30)
        ));

        assertEquals("searchId must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenFingerprintIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SearchMessage(
                "search-id-1",
                null,
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30)
        ));

        assertEquals("fingerprint must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenFingerprintIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SearchMessage(
                "search-id-1",
                "   ",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30)
        ));

        assertEquals("fingerprint must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenHotelIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SearchMessage(
                "search-id-1",
                "fingerprint-1",
                null,
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30)
        ));

        assertEquals("hotelId must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenHotelIdIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SearchMessage(
                "search-id-1",
                "fingerprint-1",
                "   ",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30)
        ));

        assertEquals("hotelId must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenCheckInIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SearchMessage(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                null,
                LocalDate.of(2023, 12, 31),
                List.of(30)
        ));

        assertEquals("checkIn must not be null", ex.getMessage());
    }

    @Test
    void shouldFailWhenCheckOutIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SearchMessage(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                null,
                List.of(30)
        ));

        assertEquals("checkOut must not be null", ex.getMessage());
    }

    @Test
    void shouldFailWhenAgesIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SearchMessage(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                null
        ));

        assertEquals("ages must not be null or empty", ex.getMessage());
    }

    @Test
    void shouldFailWhenAgesIsEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SearchMessage(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of()
        ));

        assertEquals("ages must not be null or empty", ex.getMessage());
    }

    @Test
    void shouldDefensivelyCopyAges() {
        List<Integer> ages = new ArrayList<>(List.of(30, 29, 1, 3));

        SearchMessage message = new SearchMessage(
                "search-id-1",
                "fingerprint-1",
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                ages
        );

        ages.add(99);

        assertEquals(List.of(30, 29, 1, 3), message.ages());
        assertThrows(UnsupportedOperationException.class, () -> message.ages().add(88));
    }
}
