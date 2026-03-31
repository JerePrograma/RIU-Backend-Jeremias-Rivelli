package com.riu.hotelsearch.application.port.out;

import com.riu.hotelsearch.domain.model.SearchRecord;

import java.util.Optional;

public interface FindSearchPort {
    Optional<SearchRecord> findById(String searchId);
}