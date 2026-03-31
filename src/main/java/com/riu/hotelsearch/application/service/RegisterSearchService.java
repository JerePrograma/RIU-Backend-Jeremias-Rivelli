package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.adapter.out.kafka.SearchMessage;
import com.riu.hotelsearch.application.port.in.RegisterSearchUseCase;
import com.riu.hotelsearch.application.port.out.PublishSearchEventPort;
import com.riu.hotelsearch.application.support.SearchIdGenerator;
import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.service.SearchFingerprintCalculator;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación encargado de registrar una búsqueda de forma asíncrona.
 *
 * <p>La operación genera un identificador local, calcula el fingerprint de la
 * búsqueda y publica un evento en Kafka. De este modo se evita agregar latencia
 * de base de datos al camino de la solicitud HTTP.</p>
 */
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
     * Registra una búsqueda y devuelve el identificador asignado.
     *
     * <p>La persistencia no se realiza en este punto, sino a través de la
     * publicación de un evento para procesamiento asíncrono.</p>
     *
     * @param search búsqueda validada a registrar
     * @return identificador único de la búsqueda
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