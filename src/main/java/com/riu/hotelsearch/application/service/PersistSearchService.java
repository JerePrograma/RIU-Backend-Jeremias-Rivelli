package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.adapter.out.kafka.SearchMessage;
import com.riu.hotelsearch.application.port.in.PersistSearchUseCase;
import com.riu.hotelsearch.application.port.out.SaveSearchPort;
import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.model.SearchRecord;
import com.riu.hotelsearch.domain.service.SearchFingerprintCalculator;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PersistSearchService implements PersistSearchUseCase {

    private final SaveSearchPort saveSearchPort;
    private final SearchFingerprintCalculator searchFingerprintCalculator;

    public PersistSearchService(
            SaveSearchPort saveSearchPort,
            SearchFingerprintCalculator searchFingerprintCalculator
    ) {
        this.saveSearchPort = saveSearchPort;
        this.searchFingerprintCalculator = searchFingerprintCalculator;
    }

    @Override
    public void persist(SearchMessage message) {
        Search search = new Search(
                message.hotelId(),
                message.checkIn(),
                message.checkOut(),
                message.ages()
        );

        String fingerprint = searchFingerprintCalculator.calculate(search);

        SearchRecord record = new SearchRecord(
                message.searchId(),
                search,
                fingerprint,
                Instant.now()
        );

        saveSearchPort.save(record);
    }
}