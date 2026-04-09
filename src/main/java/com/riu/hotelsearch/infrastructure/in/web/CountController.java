package com.riu.hotelsearch.infrastructure.in.web;

import com.riu.hotelsearch.infrastructure.in.web.dto.CountResponseDto;
import com.riu.hotelsearch.infrastructure.in.web.dto.ErrorResponseDto;
import com.riu.hotelsearch.infrastructure.in.web.dto.SearchDataDto;
import com.riu.hotelsearch.application.port.in.CountSearchUseCase;
import com.riu.hotelsearch.domain.model.SearchCount;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST encargado de exponer la consulta de conteo
 * de búsquedas previamente registradas.
 */
@RestController
@Validated
@Tag(
        name = "Conteo de búsquedas",
        description = "Operaciones para consultar cuántas veces se repitió una búsqueda"
)
public class CountController {

    private final CountSearchUseCase countSearchUseCase;

    public CountController(CountSearchUseCase countSearchUseCase) {
        this.countSearchUseCase = countSearchUseCase;
    }

    /**
     * Devuelve la búsqueda original asociada al identificador indicado
     * junto con el total de repeticiones equivalentes.
     */
    @Operation(
            summary = "Consultar el conteo de una búsqueda",
            description = """
                    Devuelve la búsqueda original asociada al searchId y el total de veces que se registró
                    una búsqueda equivalente.
                    
                    El conteo se resuelve por fingerprint, respetando el orden original de la lista de edades.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Conteo obtenido correctamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CountResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Respuesta exitosa",
                                    value = """
                                            {
                                              "searchId": "4ab7c9f1-3c3b-4d2c-9f12-8d8d345f0f54",
                                              "search": {
                                                "hotelId": "1234aBc",
                                                "checkIn": "29/12/2023",
                                                "checkOut": "31/12/2023",
                                                "ages": [30, 29, 1, 3]
                                              },
                                              "count": 100
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetro inválido",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Búsqueda no encontrada",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public CountResponseDto count(
            @Parameter(
                    description = "Identificador de la búsqueda generado por el endpoint POST /search",
                    required = true,
                    example = "4ab7c9f1-3c3b-4d2c-9f12-8d8d345f0f54"
            )
            @RequestParam
            @NotBlank
            String searchId
    ) {
        SearchCount result = countSearchUseCase.count(searchId);

        return new CountResponseDto(
                result.searchId(),
                new SearchDataDto(
                        result.search().hotelId(),
                        result.search().checkIn(),
                        result.search().checkOut(),
                        result.search().ages()
                ),
                result.count()
        );
    }
}