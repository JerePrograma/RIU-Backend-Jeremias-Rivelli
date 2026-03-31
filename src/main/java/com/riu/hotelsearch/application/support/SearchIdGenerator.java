package com.riu.hotelsearch.application.support;

/**
 * Componente encargado de generar identificadores únicos de búsqueda.
 */
public interface SearchIdGenerator {

    /**
     * Genera un nuevo identificador único.
     *
     * @return identificador de búsqueda
     */
    String nextId();
}