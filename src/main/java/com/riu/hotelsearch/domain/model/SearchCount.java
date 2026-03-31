package com.riu.hotelsearch.domain.model;

public record SearchCount(
        String searchId,
        Search search,
        long count
) {
}