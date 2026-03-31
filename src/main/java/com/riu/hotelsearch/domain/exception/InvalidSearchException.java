package com.riu.hotelsearch.domain.exception;

/**
 * Excepción de dominio utilizada para representar reglas de validación
 * incumplidas durante la construcción de una búsqueda.
 */
public class InvalidSearchException extends RuntimeException {
    public InvalidSearchException(String message) {
        super(message);
    }
}