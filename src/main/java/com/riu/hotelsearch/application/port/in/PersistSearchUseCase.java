package com.riu.hotelsearch.application.port.in;

import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;

/**
 * Caso de uso encargado de persistir búsquedas consumidas de forma asíncrona.
 *
 * <p>La implementación debe ser idempotente para tolerar reprocesos
 * sin duplicar registros ni conteos.</p>
 */
public interface PersistSearchUseCase {

    /**
     * Persiste el evento de búsqueda recibido.
     *
     * @param event evento de búsqueda a persistir
     */
    void persist(SearchRegisteredEvent event);
}