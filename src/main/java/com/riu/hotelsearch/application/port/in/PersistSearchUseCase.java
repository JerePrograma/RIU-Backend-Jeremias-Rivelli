package com.riu.hotelsearch.application.port.in;

import com.riu.hotelsearch.adapter.out.kafka.SearchMessage;

/**
 * Caso de uso encargado de persistir búsquedas consumidas desde Kafka.
 *
 * <p>La implementación debe ser idempotente para tolerar reprocesos
 * del consumidor sin duplicar registros ni conteos.</p>
 */
public interface PersistSearchUseCase {

    /**
     * Persiste el mensaje recibido desde Kafka.
     *
     * @param message evento de búsqueda a persistir
     */
    void persist(SearchMessage message);
}