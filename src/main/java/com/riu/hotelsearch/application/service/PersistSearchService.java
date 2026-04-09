package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;
import com.riu.hotelsearch.infrastructure.out.messaging.kafka.SearchMessage;
import com.riu.hotelsearch.application.port.in.PersistSearchUseCase;
import com.riu.hotelsearch.domain.port.out.IncrementSearchCountPort;
import com.riu.hotelsearch.domain.port.out.SaveSearchIfAbsentPort;
import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.model.SearchRecord;

import java.time.Instant;

/**
 * Servicio de aplicación encargado de persistir búsquedas consumidas desde Kafka.
 *
 * <p>La operación es transaccional para garantizar que la inserción de la búsqueda
 * y la actualización del contador agregado se confirmen o reviertan juntas.</p>
 */
public class PersistSearchService implements PersistSearchUseCase {

    private final SaveSearchIfAbsentPort saveSearchIfAbsentPort;
    private final IncrementSearchCountPort incrementSearchCountPort;

    public PersistSearchService(
            SaveSearchIfAbsentPort saveSearchIfAbsentPort,
            IncrementSearchCountPort incrementSearchCountPort
    ) {
        this.saveSearchIfAbsentPort = saveSearchIfAbsentPort;
        this.incrementSearchCountPort = incrementSearchCountPort;
    }

    /**
     * Persiste una búsqueda consumida desde Kafka.
     *
     * <p>Si la búsqueda ya había sido registrada previamente, no vuelve a insertarse
     * ni incrementa nuevamente el contador agregado.</p>
     *
     * @param event evento consumido desde Kafka
     */
    @Override
    public void persist(SearchRegisteredEvent event) {
        Search search = new Search(
                event.hotelId(),
                event.checkIn(),
                event.checkOut(),
                event.ages()
        );

        SearchRecord record = new SearchRecord(
                event.searchId(),
                search,
                event.fingerprint(),
                Instant.now()
        );

        boolean inserted = saveSearchIfAbsentPort.saveIfAbsent(record);

        if (inserted) {
            incrementSearchCountPort.increment(event.fingerprint());
        }
    }
}