package com.riu.hotelsearch.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record SearchDataDto(
        String hotelId,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkIn,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkOut,
        List<Integer> ages
) {
    public SearchDataDto {
        ages = List.copyOf(ages);
    }
}