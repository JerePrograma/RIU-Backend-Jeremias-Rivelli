package com.riu.hotelsearch.infrastructure.config;

import com.riu.hotelsearch.domain.port.out.*;
import com.riu.hotelsearch.infrastructure.config.kafka.AppKafkaProperties;
import com.riu.hotelsearch.infrastructure.out.messaging.kafka.KafkaSearchProducer;
import com.riu.hotelsearch.infrastructure.out.messaging.kafka.SearchMessage;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchCountJdbcRepository;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchReadJdbcRepository;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchRowMapper;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchWriteJdbcRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;

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
    FindSearchPort findSearchPort(SearchReadJdbcRepository repository) {
        return repository;
    }

    @Bean
    SearchWriteJdbcRepository searchWriteJdbcRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            SearchRowMapper rowMapper
    ) {
        return new SearchWriteJdbcRepository(jdbcTemplate, rowMapper);
    }

    @Bean
    SaveSearchIfAbsentPort saveSearchIfAbsentPort(SearchWriteJdbcRepository repository) {
        return repository;
    }

    @Bean
    SearchCountJdbcRepository searchCountJdbcRepository(
            NamedParameterJdbcTemplate jdbcTemplate
    ) {
        return new SearchCountJdbcRepository(jdbcTemplate);
    }

    @Bean
    GetSearchCountPort getSearchCountPort(SearchCountJdbcRepository repository) {
        return repository;
    }

    @Bean
    IncrementSearchCountPort incrementSearchCountPort(SearchCountJdbcRepository repository) {
        return repository;
    }

    @Bean
    PublishSearchEventPort publishSearchEventPort(
            KafkaTemplate<String, SearchMessage> kafkaTemplate,
            AppKafkaProperties kafkaProperties
    ) {
        return new KafkaSearchProducer(kafkaTemplate, kafkaProperties);
    }
}