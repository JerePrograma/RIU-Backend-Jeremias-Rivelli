package com.riu.hotelsearch.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(
        name = "ErrorResponse",
        description = "Estructura estándar de error devuelta por la API"
)
public record ErrorResponseDto(

        @Schema(
                description = "Fecha y hora en la que se generó el error",
                example = "2026-03-31T18:10:00Z"
        )
        Instant timestamp,

        @Schema(
                description = "Código HTTP de la respuesta",
                example = "400"
        )
        int status,

        @Schema(
                description = "Tipo general del error",
                example = "Validation error"
        )
        String error,

        @ArraySchema(
                schema = @Schema(
                        description = "Detalle de errores detectados",
                        example = "checkIn: checkIn must not be null"
                )
        )
        List<String> details
) {
}