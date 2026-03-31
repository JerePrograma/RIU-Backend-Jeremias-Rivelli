package com.riu.hotelsearch.domain.model;

import java.time.LocalDate;
import java.util.List;

public record Search(
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages
) {
    public Search {
        ages = List.copyOf(ages);
    }
}