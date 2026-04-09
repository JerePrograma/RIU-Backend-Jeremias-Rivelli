package com.riu.hotelsearch.infrastructure.support;

import com.riu.hotelsearch.domain.port.out.SearchIdGenerator;

import java.util.UUID;

/**
 * Implementación de {@link SearchIdGenerator} basada en UUID versión 4.
 *
 * <p>Se utiliza para generar identificadores únicos sin necesidad
 * de consultar la base de datos.</p>
 */
public class UuidSearchIdGenerator implements SearchIdGenerator {

    @Override
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}