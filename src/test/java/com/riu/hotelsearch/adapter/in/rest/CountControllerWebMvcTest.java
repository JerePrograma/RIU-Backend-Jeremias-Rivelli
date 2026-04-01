package com.riu.hotelsearch.adapter.in.rest;

import com.riu.hotelsearch.application.exception.SearchNotFoundException;
import com.riu.hotelsearch.application.port.in.CountSearchUseCase;
import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.model.SearchCount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountController.class)
@Import(RestExceptionHandler.class)
class CountControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @org.springframework.boot.test.mock.mockito.MockBean
    private CountSearchUseCase countSearchUseCase;

    @Test
    void shouldReturnCountWhenSearchExists() throws Exception {
        Search search = new Search(
                "1234aBc",
                LocalDate.of(2023, 12, 29),
                LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3)
        );

        when(countSearchUseCase.count("search-id-1"))
                .thenReturn(new SearchCount("search-id-1", search, 100L));

        mockMvc.perform(get("/count")
                        .param("searchId", "search-id-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.searchId").value("search-id-1"))
                .andExpect(jsonPath("$.search.hotelId").value("1234aBc"))
                .andExpect(jsonPath("$.search.ages[0]").value(30))
                .andExpect(jsonPath("$.count").value(100));
    }

    @Test
    void shouldReturnBadRequestWhenSearchIdIsBlank() throws Exception {
        mockMvc.perform(get("/count")
                        .param("searchId", " ")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.details[0]").value(containsString("must not be blank")));
    }

    @Test
    void shouldReturnNotFoundWhenSearchDoesNotExist() throws Exception {
        when(countSearchUseCase.count("missing-id"))
                .thenThrow(new SearchNotFoundException("missing-id"));

        mockMvc.perform(get("/count")
                        .param("searchId", "missing-id")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not found"))
                .andExpect(jsonPath("$.details[0]").value("Search not found: missing-id"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenUnexpectedExceptionOccurs() throws Exception {
        when(countSearchUseCase.count("search-id-2"))
                .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/count")
                        .param("searchId", "search-id-2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal error"))
                .andExpect(jsonPath("$.details[0]").value("Unexpected error"));
    }
}
