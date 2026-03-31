package com.riu.hotelsearch.adapter.in.rest.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponseDto(
        Instant timestamp,
        int status,
        String error,
        List<String> details
) {
}