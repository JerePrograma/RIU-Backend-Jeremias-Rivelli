package com.riu.hotelsearch.infrastructure.persistence.jdbc;

import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.model.SearchRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Mapper JDBC encargado de reconstruir una búsqueda persistida
 * a partir de una fila del result set.
 */
public class SearchRowMapper implements RowMapper<SearchRecord> {

    /**
     * Reconstruye un {@link SearchRecord} a partir de una fila del result set.
     *
     * @param rs result set posicionado en la fila actual
     * @param rowNum índice de fila informado por Spring JDBC
     * @return búsqueda persistida reconstruida desde la fila actual
     * @throws SQLException si ocurre un error al leer columnas del result set
     */
    @Override
    public SearchRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        Search search = new Search(
                rs.getString("hotel_id"),
                rs.getDate("check_in").toLocalDate(),
                rs.getDate("check_out").toLocalDate(),
                parseAges(rs.getString("ages_csv"))
        );

        return new SearchRecord(
                rs.getString("search_id"),
                search,
                rs.getString("fingerprint"),
                rs.getTimestamp("created_at").toInstant()
        );
    }

    /**
     * Convierte la lista de edades en su representación CSV para persistencia.
     *
     * @param ages edades de la búsqueda
     * @return representación CSV de la lista
     */
    public String toAgesCsv(List<Integer> ages) {
        return ages.stream()
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(","));
    }

    private List<Integer> parseAges(String agesCsv) {
        if (agesCsv == null || agesCsv.isBlank()) {
            return List.of();
        }

        return Arrays.stream(agesCsv.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
    }
}