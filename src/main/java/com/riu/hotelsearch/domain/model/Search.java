package com.riu.hotelsearch.domain.model;

import com.riu.hotelsearch.domain.exception.InvalidSearchException;

import java.time.LocalDate;
import java.util.List;

/**
 * Representa una búsqueda de disponibilidad de hotel validada e inmutable.
 *
 * <p>El orden de la lista de edades forma parte de la identidad lógica de la búsqueda,
 * por lo que no debe alterarse durante la construcción ni durante su uso posterior.</p>
 */
public record Search(
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages
) {
    public Search {
        validateRequiredFields(hotelId, checkIn, checkOut, ages);

        if (!checkIn.isBefore(checkOut)) {
            throw new InvalidSearchException("checkIn must be before checkOut");
        }

        validateAges(ages);
        ages = List.copyOf(ages);
    }

    private static void validateRequiredFields(
            String hotelId,
            LocalDate checkIn,
            LocalDate checkOut,
            List<Integer> ages
    ) {
        if (hotelId == null || hotelId.isBlank()) {
            throw new InvalidSearchException("hotelId must not be blank");
        }
        if (checkIn == null) {
            throw new InvalidSearchException("checkIn must not be null");
        }
        if (checkOut == null) {
            throw new InvalidSearchException("checkOut must not be null");
        }
        if (ages == null) {
            throw new InvalidSearchException("ages must not be null");
        }
    }

    private static void validateAges(List<Integer> ages) {
        if (ages.isEmpty()) {
            throw new InvalidSearchException("ages must not be empty");
        }
        for (Integer age : ages) {
            if (age == null) {
                throw new InvalidSearchException("ages must not contain null values");
            }
            if (age < 0) {
                throw new InvalidSearchException("ages must contain non-negative values");
            }
        }
    }
}