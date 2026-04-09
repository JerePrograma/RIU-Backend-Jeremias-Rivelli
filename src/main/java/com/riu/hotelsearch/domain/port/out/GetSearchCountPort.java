package com.riu.hotelsearch.domain.port.out;

/**
 * Puerto de salida encargado de consultar el contador agregado
 * asociado a un fingerprint.
 */
public interface GetSearchCountPort {

    /**
     * Obtiene la cantidad de búsquedas equivalentes registradas
     * para el fingerprint indicado.
     *
     * @param fingerprint fingerprint de la búsqueda
     * @return cantidad acumulada de búsquedas equivalentes
     */
    long getByFingerprint(String fingerprint);
}