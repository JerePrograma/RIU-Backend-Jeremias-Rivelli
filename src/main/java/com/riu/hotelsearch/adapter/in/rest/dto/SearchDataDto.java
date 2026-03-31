package com.riu.hotelsearch.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(
        name = "SearchData",
        description = "Datos de una búsqueda registrada"
)
public record SearchDataDto(

        @Schema(
                description = "Identificador del hotel consultado",
                example = "1234aBc"
        )
        String hotelId,

        @Schema(
                description = "Fecha de check-in en formato dd/MM/yyyy",
                example = "29/12/2023",
                type = "string",
                pattern = "dd/MM/yyyy"
        )
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkIn,

        @Schema(
                description = "Fecha de check-out en formato dd/MM/yyyy",
                example = "31/12/2023",
                type = "string",
                pattern = "dd/MM/yyyy"
        )
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkOut,

        @ArraySchema(
                schema = @Schema(
                        description = "Edad individual incluida en la búsqueda",
                        example = "30"
                ),
                arraySchema = @Schema(
                        description = "Lista de edades en el orden original de la búsqueda",
                        example = "[30, 29, 1, 3]"
                )
        )
        List<Integer> ages
) {
    public SearchDataDto {
        if (ages != null) {
            ages = List.copyOf(ages);
        }
    }
}