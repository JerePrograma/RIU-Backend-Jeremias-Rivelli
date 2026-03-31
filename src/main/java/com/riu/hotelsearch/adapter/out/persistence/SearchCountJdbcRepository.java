package com.riu.hotelsearch.adapter.out.persistence;

import com.riu.hotelsearch.application.port.out.GetSearchCountPort;
import com.riu.hotelsearch.application.port.out.IncrementSearchCountPort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SearchCountJdbcRepository implements IncrementSearchCountPort, GetSearchCountPort {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SearchCountJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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