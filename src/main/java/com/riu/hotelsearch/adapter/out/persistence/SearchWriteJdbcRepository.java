package com.riu.hotelsearch.adapter.out.persistence;

import com.riu.hotelsearch.application.port.out.SaveSearchIfAbsentPort;
import com.riu.hotelsearch.domain.model.SearchRecord;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class SearchWriteJdbcRepository implements SaveSearchIfAbsentPort {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SearchRowMapper rowMapper;

    public SearchWriteJdbcRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            SearchRowMapper rowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public boolean saveIfAbsent(SearchRecord record) {
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

        try {
            return jdbcTemplate.update(sql, params) == 1;
        } catch (DuplicateKeyException ex) {
            return false;
        }
    }
}