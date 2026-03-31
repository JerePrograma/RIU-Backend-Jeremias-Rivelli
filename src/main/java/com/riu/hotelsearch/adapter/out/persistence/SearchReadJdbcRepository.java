package com.riu.hotelsearch.adapter.out.persistence;

import com.riu.hotelsearch.application.port.out.FindSearchPort;
import com.riu.hotelsearch.domain.model.SearchRecord;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SearchReadJdbcRepository implements FindSearchPort {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SearchRowMapper rowMapper;

    public SearchReadJdbcRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            SearchRowMapper rowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public Optional<SearchRecord> findById(String searchId) {
        String sql = """
                select search_id, hotel_id, check_in, check_out, ages_csv, fingerprint, created_at
                from searches
                where search_id = :searchId
                """;

        var params = new MapSqlParameterSource("searchId", searchId);
        var result = jdbcTemplate.query(sql, params, rowMapper);

        return result.stream().findFirst();
    }
}