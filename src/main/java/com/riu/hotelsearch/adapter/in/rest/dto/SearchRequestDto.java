package com.riu.hotelsearch.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record SearchRequestDto(
        @NotBlank(message = "hotelId must not be blank")
        String hotelId,

        @NotNull(message = "checkIn must not be null")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkIn,

        @NotNull(message = "checkOut must not be null")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkOut,

        @NotEmpty(message = "ages must not be empty")
        List<@NotNull(message = "age must not be null") @Min(value = 0, message = "age must be >= 0") Integer> ages
) {
    public SearchRequestDto {
        if (ages != null) {
            ages = List.copyOf(ages);
        }
    }
}