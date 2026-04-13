package com.riu.hotelsearch.domain.event;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchRegisteredEventTest {

    @Test
    void shouldCreateEventWhenValid() {
        SearchRegisteredEvent event = new SearchRegisteredEvent(
                "id-1",
                "fp-1",
                "hotel-1",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        assertEquals("id-1", event.searchId());
        assertEquals("fp-1", event.fingerprint());
        assertEquals("hotel-1", event.hotelId());
        assertEquals(LocalDate.of(2023, 12, 29), event.checkIn());
        assertEquals(LocalDate.of(2023, 12, 31), event.checkOut());
        assertEquals(List.of(30, 29, 1, 3), event.ages());
    }

    @Test
    void shouldFailWhenSearchIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new SearchRegisteredEvent(
                        null,
                        "fp-1",
                        "hotel-1",
                        LocalDate.of(2023, 12, 29),
                        LocalDate.of(2023, 12, 31),
                        List.of(30)
                )
        );

        assertEquals("searchId must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenSearchIdIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new SearchRegisteredEvent(
                        "   ",
                        "fp-1",
                        "hotel-1",
                        LocalDate.of(2023, 12, 29),
                        LocalDate.of(2023, 12, 31),
                        List.of(30)
                )
        );

        assertEquals("searchId must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenFingerprintIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new SearchRegisteredEvent(
                        "id-1",
                        null,
                        "hotel-1",
                        LocalDate.of(2023, 12, 29),
                        LocalDate.of(2023, 12, 31),
                        List.of(30)
                )
        );

        assertEquals("fingerprint must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenFingerprintIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new SearchRegisteredEvent(
                        "id-1",
                        "   ",
                        "hotel-1",
                        LocalDate.of(2023, 12, 29),
                        LocalDate.of(2023, 12, 31),
                        List.of(30)
                )
        );

        assertEquals("fingerprint must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenHotelIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new SearchRegisteredEvent(
                        "id-1",
                        "fp-1",
                        null,
                        LocalDate.of(2023, 12, 29),
                        LocalDate.of(2023, 12, 31),
                        List.of(30)
                )
        );

        assertEquals("hotelId must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenHotelIdIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new SearchRegisteredEvent(
                        "id-1",
                        "fp-1",
                        "   ",
                        LocalDate.of(2023, 12, 29),
                        LocalDate.of(2023, 12, 31),
                        List.of(30)
                )
        );

        assertEquals("hotelId must not be blank", ex.getMessage());
    }

    @Test
    void shouldFailWhenCheckInIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new SearchRegisteredEvent(
                        "id-1",
                        "fp-1",
                        "hotel-1",
                        null,
                        LocalDate.of(2023, 12, 31),
                        List.of(30)
                )
        );

        assertEquals("checkIn must not be null", ex.getMessage());
    }

    @Test
    void shouldFailWhenCheckOutIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new SearchRegisteredEvent(
                        "id-1",
                        "fp-1",
                        "hotel-1",
                        LocalDate.of(2023, 12, 29),
                        null,
                        List.of(30)
                )
        );

        assertEquals("checkOut must not be null", ex.getMessage());
    }

    @Test
    void shouldFailWhenAgesIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new SearchRegisteredEvent(
                        "id-1",
                        "fp-1",
                        "hotel-1",
                        LocalDate.of(2023, 12, 29),
                        LocalDate.of(2023, 12, 31),
                        null
                )
        );

        assertEquals("ages must not be null or empty", ex.getMessage());
    }

    @Test
    void shouldFailWhenAgesIsEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new SearchRegisteredEvent(
                        "id-1",
                        "fp-1",
                        "hotel-1",
                        LocalDate.of(2023, 12, 29),
                        LocalDate.of(2023, 12, 31),
                        List.of()
                )
        );

        assertEquals("ages must not be null or empty", ex.getMessage());
    }

    @Test
    void shouldMakeDefensiveCopyOfAges() {
        List<Integer> originalAges = new ArrayList<>(List.of(30, 29, 1, 3));

        SearchRegisteredEvent event = new SearchRegisteredEvent(
                "id-1",
                "fp-1",
                "hotel-1",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                originalAges
        );

        originalAges.add(99);

        assertEquals(List.of(30, 29, 1, 3), event.ages());
        assertThrows(UnsupportedOperationException.class, () -> event.ages().add(88));
    }
}