package com.riu.hotelsearch.infrastructure.config;

import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchCountJdbcRepository;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchReadJdbcRepository;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchRowMapper;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchWriteJdbcRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class InfrastructureAdaptersConfiguration {

    @Bean
    SearchRowMapper searchRowMapper() {
        return new SearchRowMapper();
    }

    @Bean
    SearchReadJdbcRepository searchReadJdbcRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            SearchRowMapper rowMapper
    ) {
        return new SearchReadJdbcRepository(jdbcTemplate, rowMapper);
    }

    @Bean
    SearchWriteJdbcRepository searchWriteJdbcRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            SearchRowMapper rowMapper
    ) {
        return new SearchWriteJdbcRepository(jdbcTemplate, rowMapper);
    }

    @Bean
    SearchCountJdbcRepository searchCountJdbcRepository(
            NamedParameterJdbcTemplate jdbcTemplate
    ) {
        return new SearchCountJdbcRepository(jdbcTemplate);
    }
}