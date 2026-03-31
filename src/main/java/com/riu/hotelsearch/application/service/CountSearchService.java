package com.riu.hotelsearch.application.service;

import com.riu.hotelsearch.application.exception.SearchNotFoundException;
import com.riu.hotelsearch.application.port.in.CountSearchUseCase;
import com.riu.hotelsearch.application.port.out.FindSearchPort;
import com.riu.hotelsearch.application.port.out.GetSearchCountPort;
import com.riu.hotelsearch.domain.model.SearchCount;
import com.riu.hotelsearch.domain.model.SearchRecord;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación encargado de consultar el total de búsquedas
 * equivalentes a partir de un searchId.
 */
@Service
public class CountSearchService implements CountSearchUseCase {

    private final FindSearchPort findSearchPort;
    private final GetSearchCountPort getSearchCountPort;

    public CountSearchService(
            FindSearchPort findSearchPort,
            GetSearchCountPort getSearchCountPort
    ) {
        this.findSearchPort = findSearchPort;
        this.getSearchCountPort = getSearchCountPort;
    }

    /**
     * Obtiene la búsqueda original asociada al identificador indicado y resuelve
     * su conteo agregado a partir del fingerprint persistido.
     *
     * @param searchId identificador de la búsqueda
     * @return búsqueda original junto con la cantidad de repeticiones
     * @throws SearchNotFoundException si no existe una búsqueda con ese identificador
     */
    @Override
    public SearchCount count(String searchId) {
        SearchRecord record = findSearchPort.findById(searchId)
                .orElseThrow(() -> new SearchNotFoundException(searchId));

        long count = getSearchCountPort.getByFingerprint(record.fingerprint());

        return new SearchCount(record.searchId(), record.search(), count);
    }
}