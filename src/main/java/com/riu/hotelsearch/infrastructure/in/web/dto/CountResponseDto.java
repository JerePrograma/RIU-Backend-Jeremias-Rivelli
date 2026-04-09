package com.riu.hotelsearch.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "CountResponse",
        description = "Respuesta del endpoint de conteo con la búsqueda original y la cantidad de repeticiones equivalentes"
)
public record CountResponseDto(

        @Schema(
                description = "Identificador único de la búsqueda consultada",
                example = "4ab7c9f1-3c3b-4d2c-9f12-8d8d345f0f54"
        )
        String searchId,

        @Schema(
                description = "Datos originales de la búsqueda asociada al identificador consultado"
        )
        SearchDataDto search,

        @Schema(
                description = "Cantidad de veces que se registró una búsqueda equivalente",
                example = "100"
        )
        long count
) {
}