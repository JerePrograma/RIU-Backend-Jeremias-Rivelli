package com.riu.hotelsearch.domain.model;

/**
 * Representa el resultado de una consulta de conteo para una búsqueda registrada.
 */
public record SearchCount(
        String searchId,
        Search search,
        long count
) {
}