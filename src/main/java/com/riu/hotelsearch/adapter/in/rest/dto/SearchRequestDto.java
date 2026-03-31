package com.riu.hotelsearch.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(
        name = "SearchRequest",
        description = "Request para registrar una búsqueda de disponibilidad de hotel"
)
public record SearchRequestDto(

        @Schema(
                description = "Identificador del hotel a consultar",
                example = "1234aBc"
        )
        @NotBlank(message = "hotelId must not be blank")
        String hotelId,

        @Schema(
                description = "Fecha de check-in en formato dd/MM/yyyy",
                example = "29/12/2023",
                type = "string",
                pattern = "dd/MM/yyyy"
        )
        @NotNull(message = "checkIn must not be null")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkIn,

        @Schema(
                description = "Fecha de check-out en formato dd/MM/yyyy",
                example = "31/12/2023",
                type = "string",
                pattern = "dd/MM/yyyy"
        )
        @NotNull(message = "checkOut must not be null")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate checkOut,

        @ArraySchema(
                schema = @Schema(
                        description = "Lista de edades. El orden forma parte de la identidad de la búsqueda",
                        example = "30"
                ),
                arraySchema = @Schema(
                        description = "Lista de edades asociadas a la búsqueda",
                        example = "[30, 29, 1, 3]"
                )
        )
        @NotEmpty(message = "ages must not be empty")
        List<
                @NotNull(message = "age must not be null")
                @Min(value = 0, message = "age must be >= 0")
                        Integer> ages
) {
    public SearchRequestDto {
        if (ages != null) {
            ages = List.copyOf(ages);
        }
    }
}