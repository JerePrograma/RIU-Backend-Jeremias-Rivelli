package com.riu.hotelsearch.domain.port.out;

/**
 * Puerto de salida encargado de actualizar el contador agregado
 * de búsquedas equivalentes para un fingerprint determinado.
 */
public interface IncrementSearchCountPort {

    /**
     * Incrementa en una unidad el contador asociado al fingerprint indicado.
     *
     * @param fingerprint fingerprint de la búsqueda
     */
    void increment(String fingerprint);
}