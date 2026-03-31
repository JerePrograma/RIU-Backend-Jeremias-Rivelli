package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.adapter.out.kafka.SearchMessage;
import com.riu.hotelsearch.application.port.in.RegisterSearchUseCase;
import com.riu.hotelsearch.application.port.out.PublishSearchEventPort;
import com.riu.hotelsearch.application.support.SearchIdGenerator;
import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.service.SearchFingerprintCalculator;
import org.springframework.stereotype.Service;

@Service
public class RegisterSearchService implements RegisterSearchUseCase {

    private final PublishSearchEventPort publishSearchEventPort;
    private final SearchIdGenerator searchIdGenerator;
    private final SearchFingerprintCalculator searchFingerprintCalculator;

    public RegisterSearchService(
            PublishSearchEventPort publishSearchEventPort,
            SearchIdGenerator searchIdGenerator,
            SearchFingerprintCalculator searchFingerprintCalculator
    ) {
        this.publishSearchEventPort = publishSearchEventPort;
        this.searchIdGenerator = searchIdGenerator;
        this.searchFingerprintCalculator = searchFingerprintCalculator;
    }

    /**
     * Registers the search asynchronously by publishing it to Kafka.
     * The identifier is generated locally to avoid adding database latency to the HTTP request path.
     */
    @Override
    public String register(Search search) {
        String searchId = searchIdGenerator.nextId();
        String fingerprint = searchFingerprintCalculator.calculate(search);

        publishSearchEventPort.publish(toMessage(searchId, fingerprint, search));
        return searchId;
    }

    private SearchMessage toMessage(String searchId, String fingerprint, Search search) {
        return new SearchMessage(
                searchId,
                fingerprint,
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                search.ages()
        );
    }
}