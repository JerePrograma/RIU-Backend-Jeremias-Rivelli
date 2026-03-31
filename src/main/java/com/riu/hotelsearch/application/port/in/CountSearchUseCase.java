package com.riu.hotelsearch.application.port.in;

import com.riu.hotelsearch.domain.model.SearchCount;

/**
 * Caso de uso encargado de consultar cuántas veces se repitió
 * una búsqueda previamente registrada.
 */
public interface CountSearchUseCase {

    /**
     * Obtiene la búsqueda asociada al identificador indicado y su conteo agregado.
     *
     * @param searchId identificador de la búsqueda
     * @return búsqueda original junto con su cantidad de repeticiones
     */
    SearchCount count(String searchId);
}