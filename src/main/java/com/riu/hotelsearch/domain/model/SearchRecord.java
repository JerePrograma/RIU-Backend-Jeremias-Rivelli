package com.riu.hotelsearch.domain.model;

import java.time.Instant;

/**
 * Representa una búsqueda persistida junto con su fingerprint y fecha de creación.
 */
public record SearchRecord(
        String searchId,
        Search search,
        String fingerprint,
        Instant createdAt
) {
}