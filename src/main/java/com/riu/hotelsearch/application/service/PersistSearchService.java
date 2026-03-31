package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.adapter.out.kafka.SearchMessage;
import com.riu.hotelsearch.application.port.in.PersistSearchUseCase;
import com.riu.hotelsearch.application.port.out.IncrementSearchCountPort;
import com.riu.hotelsearch.application.port.out.SaveSearchIfAbsentPort;
import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.model.SearchRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Servicio de aplicación encargado de persistir búsquedas consumidas desde Kafka.
 *
 * <p>La operación es transaccional para garantizar que la inserción de la búsqueda
 * y la actualización del contador agregado se confirmen o reviertan juntas.</p>
 */
@Service
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
     * @param message evento consumido desde Kafka
     */
    @Override
    @Transactional
    public void persist(SearchMessage message) {
        Search search = new Search(
                message.hotelId(),
                message.checkIn(),
                message.checkOut(),
                message.ages()
        );

        SearchRecord record = new SearchRecord(
                message.searchId(),
                search,
                message.fingerprint(),
                Instant.now()
        );

        boolean inserted = saveSearchIfAbsentPort.saveIfAbsent(record);

        if (inserted) {
            incrementSearchCountPort.increment(message.fingerprint());
        }
    }
}