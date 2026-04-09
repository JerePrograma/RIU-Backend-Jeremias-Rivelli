package com.riu.hotelsearch.domain.event;

import java.time.LocalDate;
import java.util.List;

public record SearchRegisteredEvent(
        String searchId,
        String fingerprint,
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages
) {
    public SearchRegisteredEvent {
        if (searchId == null || searchId.isBlank()) {
            throw new IllegalArgumentException("searchId must not be blank");
        }
        if (fingerprint == null || fingerprint.isBlank()) {
            throw new IllegalArgumentException("fingerprint must not be blank");
        }
        if (hotelId == null || hotelId.isBlank()) {
            throw new IllegalArgumentException("hotelId must not be blank");
        }
        if (checkIn == null) {
            throw new IllegalArgumentException("checkIn must not be null");
        }
        if (checkOut == null) {
            throw new IllegalArgumentException("checkOut must not be null");
        }
        if (ages == null || ages.isEmpty()) {
            throw new IllegalArgumentException("ages must not be null or empty");
        }

        ages = List.copyOf(ages);
    }
}