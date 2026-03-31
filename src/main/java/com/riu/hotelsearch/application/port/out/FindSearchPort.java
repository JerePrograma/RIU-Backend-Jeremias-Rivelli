package com.riu.hotelsearch.application.port.out;

import com.riu.hotelsearch.domain.model.SearchRecord;

import java.util.Optional;

/**
 * Puerto de salida encargado de recuperar una búsqueda persistida
 * a partir de su identificador.
 */
public interface FindSearchPort {

    /**
     * Busca una búsqueda por su searchId.
     *
     * @param searchId identificador de la búsqueda
     * @return búsqueda encontrada, o vacío si no existe
     */
    Optional<SearchRecord> findById(String searchId);
}