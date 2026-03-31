package com.riu.hotelsearch.domain.service;

import com.riu.hotelsearch.domain.model.Search;

/**
 * Servicio de dominio encargado de calcular una representación canónica
 * de una búsqueda para agrupar búsquedas equivalentes.
 */
public interface SearchFingerprintCalculator {

    /**
     * Calcula el fingerprint de una búsqueda.
     *
     * @param search búsqueda validada
     * @return fingerprint determinístico de la búsqueda
     */
    String calculate(Search search);
}