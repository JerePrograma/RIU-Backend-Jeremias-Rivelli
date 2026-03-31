package com.riu.hotelsearch.adapter.in.rest.dto;

public record CountResponseDto(
        String searchId,
        SearchDataDto search,
        long count
) {
}