package com.riu.hotelsearch.domain.port.out;

import com.riu.hotelsearch.domain.model.SearchRecord;

/**
 * Puerto de salida encargado de persistir una búsqueda únicamente
 * si todavía no existe un registro con el mismo identificador.
 */
public interface SaveSearchIfAbsentPort {

    /**
     * Persiste la búsqueda si no existe previamente.
     *
     * @param record búsqueda a persistir
     * @return {@code true} si la búsqueda fue insertada,
     *         {@code false} si ya existía
     */
    boolean saveIfAbsent(SearchRecord record);
}