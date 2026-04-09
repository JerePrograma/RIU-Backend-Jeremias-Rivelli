package com.riu.hotelsearch.infrastructure.config;

import com.riu.hotelsearch.infrastructure.config.OpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    private final OpenApiConfig config = new OpenApiConfig();

    @Test
    void shouldBuildOpenApiWithExpectedMetadata() {
        OpenAPI openAPI = config.openAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Hotel Search API", openAPI.getInfo().getTitle());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
        assertTrue(openAPI.getInfo().getDescription().contains("POST /search"));
        assertNotNull(openAPI.getExternalDocs());
        assertEquals("Documentación del proyecto", openAPI.getExternalDocs().getDescription());
        assertEquals("REEMPLAZAR_POR_URL_DEL_REPOSITORIO", openAPI.getExternalDocs().getUrl());
    }
}
