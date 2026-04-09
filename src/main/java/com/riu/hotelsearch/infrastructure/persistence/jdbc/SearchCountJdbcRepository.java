package com.riu.hotelsearch.infrastructure.persistence.jdbc;

import com.riu.hotelsearch.domain.port.out.GetSearchCountPort;
import com.riu.hotelsearch.domain.port.out.IncrementSearchCountPort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Repositorio JDBC encargado de mantener y consultar el contador agregado
 * de búsquedas equivalentes por fingerprint.
 */
public class SearchCountJdbcRepository implements IncrementSearchCountPort, GetSearchCountPort {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SearchCountJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Incrementa el contador agregado asociado al fingerprint indicado.
     *
     * <p>La actualización se realiza mediante MERGE para resolver de forma
     * atómica la inserción inicial o el incremento posterior.</p>
     *
     * @param fingerprint fingerprint de la búsqueda
     */
    @Override
    public void increment(String fingerprint) {
        String sql = """
                merge into search_counts sc
                using (select :fingerprint as fingerprint from dual) src
                on (sc.fingerprint = src.fingerprint)
                when matched then
                    update set sc.total_count = sc.total_count + 1
                when not matched then
                    insert (fingerprint, total_count)
                    values (src.fingerprint, 1)
                """;

        var params = new MapSqlParameterSource("fingerprint", fingerprint);
        jdbcTemplate.update(sql, params);
    }

    /**
     * Obtiene el contador agregado asociado al fingerprint indicado.
     *
     * @param fingerprint fingerprint de la búsqueda
     * @return cantidad de búsquedas equivalentes registradas
     */
    @Override
    public long getByFingerprint(String fingerprint) {
        String sql = """
                select total_count
                from search_counts
                where fingerprint = :fingerprint
                """;

        var params = new MapSqlParameterSource("fingerprint", fingerprint);
        Long count = jdbcTemplate.queryForObject(sql, params, Long.class);
        return count == null ? 0L : count;
    }
}