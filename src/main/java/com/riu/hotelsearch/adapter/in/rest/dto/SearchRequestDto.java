package com.riu.hotelsearch.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record SearchRequestDto(
        @NotBlank
        String hotelId,

        @NotNull
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkIn,

        @NotNull
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkOut,

        @NotNull
        @NotEmpty
        List<Integer> ages
) {
    public SearchRequestDto {
        ages = List.copyOf(ages);
    }
}