package com.riu.hotelsearch.domain.model;

import java.time.Instant;

public record SearchRecord(
        String searchId,
        Search search,
        String fingerprint,
        Instant createdAt
) {
}