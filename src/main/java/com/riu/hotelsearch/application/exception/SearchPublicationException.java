package com.riu.hotelsearch.application.exception;

/**
 * Excepción lanzada cuando no se puede publicar una búsqueda
 * para su procesamiento asíncrono.
 */
public class SearchPublicationException extends RuntimeException {

    public SearchPublicationException(String message, Throwable cause) {
        super(message, cause);
    }
}