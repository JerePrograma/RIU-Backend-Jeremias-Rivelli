package com.riu.hotelsearch.domain.port.out;

import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;

/**
 * Puerto de salida encargado de publicar eventos de búsqueda
 * hacia la infraestructura de mensajería.
 */
public interface PublishSearchEventPort {

    /**
     * Publica un evento de búsqueda para su procesamiento asíncrono.
     *
     * @param event evento a publicar
     */
    void publish(SearchRegisteredEvent event);
}