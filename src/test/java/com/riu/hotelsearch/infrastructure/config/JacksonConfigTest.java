package com.riu.hotelsearch.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riu.hotelsearch.infrastructure.config.JacksonConfig;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JacksonConfigTest {

    private final JacksonConfig config = new JacksonConfig();

    @Test
    void shouldSerializeJavaTimeWithoutTimestamps() throws Exception {
        ObjectMapper mapper = config.objectMapper();

        String json = mapper.writeValueAsString(Map.of("date", LocalDate.of(2023, 12, 29)));

        assertEquals("{\"date\":\"2023-12-29\"}", json);
    }

    @Test
    void shouldDeserializeJavaTimeTypes() throws Exception {
        ObjectMapper mapper = config.objectMapper();

        Payload payload = mapper.readValue("{\"date\":\"2023-12-29\"}", Payload.class);

        assertEquals(LocalDate.of(2023, 12, 29), payload.date());
    }

    record Payload(LocalDate date) {
    }
}
