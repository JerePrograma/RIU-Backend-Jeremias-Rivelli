package com.riu.hotelsearch.adapter.out.persistence;

import com.riu.hotelsearch.application.port.out.CountSearchesPort;
import com.riu.hotelsearch.application.port.out.FindSearchPort;
import com.riu.hotelsearch.application.port.out.SaveSearchPort;
import com.riu.hotelsearch.domain.model.SearchRecord;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class SearchJdbcRepository implements SaveSearchPort, FindSearchPort, CountSearchesPort {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SearchRowMapper rowMapper;

    public SearchJdbcRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            SearchRowMapper rowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public void save(SearchRecord record) {
        String sql = """
                insert into searches (
                    search_id,
                    hotel_id,
                    check_in,
                    check_out,
                    ages_csv,
                    fingerprint,
                    created_at
                ) values (
                    :searchId,
                    :hotelId,
                    :checkIn,
                    :checkOut,
                    :agesCsv,
                    :fingerprint,
                    :createdAt
                )
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("searchId", record.searchId())
                .addValue("hotelId", record.search().hotelId())
                .addValue("checkIn", record.search().checkIn())
                .addValue("checkOut", record.search().checkOut())
                .addValue("agesCsv", rowMapper.toAgesCsv(record.search().ages()))
                .addValue("fingerprint", record.fingerprint())
                .addValue("createdAt", Timestamp.from(record.createdAt()));

        jdbcTemplate.update(sql, params);
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

    @Override
    public long countByFingerprint(String fingerprint) {
        String sql = """
                select count(*)
                from searches
                where fingerprint = :fingerprint
                """;

        var params = new MapSqlParameterSource("fingerprint", fingerprint);
        Long count = jdbcTemplate.queryForObject(sql, params, Long.class);
        return count == null ? 0L : count;
    }
}