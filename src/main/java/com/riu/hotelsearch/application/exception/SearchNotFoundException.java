package com.riu.hotelsearch.application.exception;

/**
 * Excepción lanzada cuando no existe una búsqueda asociada al searchId indicado.
 */
public class SearchNotFoundException extends RuntimeException {
    public SearchNotFoundException(String searchId) {
        super("Search not found: " + searchId);
    }
}