package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.application.port.in.RegisterSearchUseCase;
import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;
import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.port.out.PublishSearchEventPort;
import com.riu.hotelsearch.domain.port.out.SearchIdGenerator;
import com.riu.hotelsearch.domain.service.SearchFingerprintCalculator;

/**
 * Servicio de aplicación encargado de registrar una búsqueda de forma asíncrona.
 *
 * <p>La operación genera un identificador local, calcula el fingerprint y publica
 * un evento de dominio para su procesamiento asíncrono.</p>
 */
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

    @Override
    public String register(Search search) {
        String searchId = searchIdGenerator.nextId();
        String fingerprint = searchFingerprintCalculator.calculate(search);

        publishSearchEventPort.publish(toEvent(searchId, fingerprint, search));
        return searchId;
    }

    private SearchRegisteredEvent toEvent(String searchId, String fingerprint, Search search) {
        return new SearchRegisteredEvent(
                searchId,
                fingerprint,
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                search.ages()
        );
    }
}