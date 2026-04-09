package com.riu.hotelsearch.infrastructure.in.web;

import com.riu.hotelsearch.application.port.in.RegisterSearchUseCase;
import com.riu.hotelsearch.domain.model.Search;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
@Import(RestExceptionHandler.class)
class SearchControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterSearchUseCase registerSearchUseCase;

    @Test
    void shouldAcceptValidSearchRequest() throws Exception {
        when(registerSearchUseCase.register(any(Search.class))).thenReturn("search-id-1");

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "hotelId": "1234aBc",
                                  "checkIn": "29/12/2023",
                                  "checkOut": "31/12/2023",
                                  "ages": [30, 29, 1, 3]
                                }
                                """))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.searchId").value("search-id-1"));
    }

    @Test
    void shouldReturnBadRequestWhenHotelIdIsBlank() throws Exception {
        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "hotelId": "   ",
                                  "checkIn": "29/12/2023",
                                  "checkOut": "31/12/2023",
                                  "ages": [30, 29, 1, 3]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.details[0]").value(containsString("hotelId must not be blank")));
    }

    @Test
    void shouldReturnBadRequestWhenAgesIsEmpty() throws Exception {
        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "hotelId": "1234aBc",
                                  "checkIn": "29/12/2023",
                                  "checkOut": "31/12/2023",
                                  "ages": []
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.details[0]").value(containsString("ages must not be empty")));
    }

    @Test
    void shouldReturnBadRequestWhenAgeIsNegative() throws Exception {
        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "hotelId": "1234aBc",
                                  "checkIn": "29/12/2023",
                                  "checkOut": "31/12/2023",
                                  "ages": [30, -1, 3]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.details[0]").value(containsString("age must be >= 0")));
    }

    @Test
    void shouldReturnBadRequestWhenDateFormatIsInvalid() throws Exception {
        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "hotelId": "1234aBc",
                                  "checkIn": "2023-12-29",
                                  "checkOut": "31/12/2023",
                                  "ages": [30, 29, 1, 3]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed request"))
                .andExpect(jsonPath("$.details[0]").value("Request body is invalid or contains malformed date values"));
    }

    @Test
    @DisplayName("Debe devolver 400 cuando la regla de dominio checkIn < checkOut no se cumple")
    void shouldReturnBadRequestWhenDomainValidationFails() throws Exception {
        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "hotelId": "1234aBc",
                                  "checkIn": "31/12/2023",
                                  "checkOut": "31/12/2023",
                                  "ages": [30, 29, 1, 3]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.details[0]").value("checkIn must be before checkOut"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenUnexpectedExceptionOccurs() throws Exception {
        when(registerSearchUseCase.register(any(Search.class))).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "hotelId": "1234aBc",
                                  "checkIn": "29/12/2023",
                                  "checkOut": "31/12/2023",
                                  "ages": [30, 29, 1, 3]
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal error"))
                .andExpect(jsonPath("$.details[0]").value("Unexpected error"));
    }
}