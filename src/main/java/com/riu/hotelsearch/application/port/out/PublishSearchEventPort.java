package com.riu.hotelsearch.application.port.out;

import com.riu.hotelsearch.adapter.out.kafka.SearchMessage;

/**
 * Puerto de salida encargado de publicar eventos de búsqueda
 * hacia la infraestructura de mensajería.
 */
public interface PublishSearchEventPort {

    /**
     * Publica un evento de búsqueda para su procesamiento asíncrono.
     *
     * @param message evento a publicar
     */
    void publish(SearchMessage message);
}