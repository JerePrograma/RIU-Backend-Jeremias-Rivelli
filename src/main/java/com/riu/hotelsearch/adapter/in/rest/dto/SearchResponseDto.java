package com.riu.hotelsearch.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "SearchResponse",
        description = "Respuesta del endpoint de registro de búsqueda"
)
public record SearchResponseDto(

        @Schema(
                description = "Identificador único generado para la búsqueda aceptada",
                example = "4ab7c9f1-3c3b-4d2c-9f12-8d8d345f0f54"
        )
        String searchId
) {
}