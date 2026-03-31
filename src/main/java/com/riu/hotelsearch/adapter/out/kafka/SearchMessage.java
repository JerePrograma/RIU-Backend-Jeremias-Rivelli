package com.riu.hotelsearch.adapter.out.kafka;

import java.time.LocalDate;
import java.util.List;

/**
 * Evento de búsqueda intercambiado entre el productor y el consumidor Kafka.
 *
 * <p>Incluye tanto el identificador técnico de la búsqueda como el fingerprint
 * utilizado para agrupar búsquedas equivalentes.</p>
 */
public record SearchMessage(
        String searchId,
        String fingerprint,
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages
) {
    public SearchMessage {
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