package com.riu.hotelsearch.application.port.in;

import com.riu.hotelsearch.domain.model.Search;

/**
 * Caso de uso encargado de registrar una búsqueda de disponibilidad.
 *
 * <p>La operación devuelve un identificador único de búsqueda y desacopla
 * la persistencia real mediante publicación asíncrona de un evento.</p>
 */
public interface RegisterSearchUseCase {

    /**
     * Registra una búsqueda y devuelve el identificador asignado.
     *
     * @param search búsqueda validada a registrar
     * @return identificador único de la búsqueda
     */
    String register(Search search);
}