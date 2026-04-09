package com.riu.hotelsearch.infrastructure.in.web;

import com.riu.hotelsearch.infrastructure.in.web.dto.ErrorResponseDto;
import com.riu.hotelsearch.infrastructure.in.web.dto.SearchRequestDto;
import com.riu.hotelsearch.infrastructure.in.web.dto.SearchResponseDto;
import com.riu.hotelsearch.application.port.in.RegisterSearchUseCase;
import com.riu.hotelsearch.domain.model.Search;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST encargado de exponer el registro de búsquedas
 * de disponibilidad de hotel.
 */
@RestController
@RequestMapping("/search")
@Tag(
        name = "Búsquedas",
        description = "Operaciones para registrar búsquedas de disponibilidad de hotel"
)
public class SearchController {

    private final RegisterSearchUseCase registerSearchUseCase;

    public SearchController(RegisterSearchUseCase registerSearchUseCase) {
        this.registerSearchUseCase = registerSearchUseCase;
    }

    /**
     * Acepta una búsqueda válida, genera un identificador y delega
     * su persistencia para procesamiento asíncrono.
     */
    @Operation(
            summary = "Registrar una búsqueda",
            description = """
                    Registra una búsqueda de disponibilidad y devuelve un identificador único.
                    
                    La persistencia real se realiza de forma asíncrona a través de Kafka, por eso
                    la respuesta devuelve HTTP 202 Accepted en lugar de 200 OK.
                    
                    Importante: el orden de la lista de edades forma parte de la identidad de la búsqueda.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Búsqueda aceptada correctamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SearchResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Respuesta exitosa",
                                    value = """
                                            {
                                              "searchId": "4ab7c9f1-3c3b-4d2c-9f12-8d8d345f0f54"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request inválido",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Error de validación",
                                    value = """
                                            {
                                              "timestamp": "2026-03-31T18:10:00Z",
                                              "status": 400,
                                              "error": "Validation error",
                                              "details": [
                                                "checkIn: checkIn must not be null"
                                              ]
                                            }
                                            """
                            )
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
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SearchResponseDto search(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos de la búsqueda a registrar",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SearchRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de búsqueda",
                                    value = """
                                            {
                                              "hotelId": "1234aBc",
                                              "checkIn": "29/12/2023",
                                              "checkOut": "31/12/2023",
                                              "ages": [30, 29, 1, 3]
                                            }
                                            """
                            )
                    )
            )
            SearchRequestDto request
    ) {
        Search search = new Search(
                request.hotelId(),
                request.checkIn(),
                request.checkOut(),
                request.ages()
        );

        String searchId = registerSearchUseCase.register(search);
        return new SearchResponseDto(searchId);
    }
}