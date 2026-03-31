package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.application.port.in.CountSearchUseCase;
import com.riu.hotelsearch.application.port.out.CountSearchesPort;
import com.riu.hotelsearch.application.port.out.FindSearchPort;
import com.riu.hotelsearch.domain.model.SearchCount;
import com.riu.hotelsearch.domain.model.SearchRecord;
import org.springframework.stereotype.Service;

@Service
public class CountSearchService implements CountSearchUseCase {

    private final FindSearchPort findSearchPort;
    private final CountSearchesPort countSearchesPort;

    public CountSearchService(
            FindSearchPort findSearchPort,
            CountSearchesPort countSearchesPort
    ) {
        this.findSearchPort = findSearchPort;
        this.countSearchesPort = countSearchesPort;
    }

    @Override
    public SearchCount count(String searchId) {
        SearchRecord record = findSearchPort.findById(searchId)
                .orElseThrow(() -> new IllegalArgumentException("Search not found"));

        long count = countSearchesPort.countByFingerprint(record.fingerprint());

        return new SearchCount(record.searchId(), record.search(), count);
    }
}