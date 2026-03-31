package com.riu.hotelsearch.domain.service;

import com.riu.hotelsearch.domain.model.Search;

public interface SearchFingerprintCalculator {
    String calculate(Search search);
}