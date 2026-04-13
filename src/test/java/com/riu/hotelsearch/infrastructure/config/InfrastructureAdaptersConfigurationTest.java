package com.riu.hotelsearch.infrastructure.config;

import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchCountJdbcRepository;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchReadJdbcRepository;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchRowMapper;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchWriteJdbcRepository;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

class InfrastructureAdaptersConfigurationTest {

    private final InfrastructureAdaptersConfiguration config = new InfrastructureAdaptersConfiguration();

    @Test
    void shouldCreateSearchRowMapper() {
        SearchRowMapper bean = config.searchRowMapper();
        assertInstanceOf(SearchRowMapper.class, bean);
    }

    @Test
    void shouldCreateSearchReadJdbcRepository() {
        NamedParameterJdbcTemplate jdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        SearchRowMapper rowMapper = new SearchRowMapper();

        SearchReadJdbcRepository bean = config.searchReadJdbcRepository(jdbcTemplate, rowMapper);

        assertInstanceOf(SearchReadJdbcRepository.class, bean);
    }

    @Test
    void shouldCreateSearchWriteJdbcRepository() {
        NamedParameterJdbcTemplate jdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        SearchRowMapper rowMapper = new SearchRowMapper();

        SearchWriteJdbcRepository bean = config.searchWriteJdbcRepository(jdbcTemplate, rowMapper);

        assertInstanceOf(SearchWriteJdbcRepository.class, bean);
    }

    @Test
    void shouldCreateSearchCountJdbcRepository() {
        NamedParameterJdbcTemplate jdbcTemplate = mock(NamedParameterJdbcTemplate.class);

        SearchCountJdbcRepository bean = config.searchCountJdbcRepository(jdbcTemplate);

        assertInstanceOf(SearchCountJdbcRepository.class, bean);
    }
}