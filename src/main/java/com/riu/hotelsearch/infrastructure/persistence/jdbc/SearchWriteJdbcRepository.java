package com.riu.hotelsearch.infrastructure.persistence.jdbc;

import com.riu.hotelsearch.domain.port.out.SaveSearchIfAbsentPort;
import com.riu.hotelsearch.domain.model.SearchRecord;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * Repositorio JDBC encargado de insertar búsquedas individuales.
 *
 * <p>La operación es idempotente respecto de searchId. Si la búsqueda ya existe,
 * se considera un caso esperado y se informa como no insertada.</p>
 */
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

    /**
     * Inserta la búsqueda solo si todavía no existe un registro con el mismo searchId.
     *
     * @param record búsqueda a persistir
     * @return {@code true} si la búsqueda fue insertada, {@code false} si ya existía
     */
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