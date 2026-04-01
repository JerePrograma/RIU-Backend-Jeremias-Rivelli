package com.riu.hotelsearch.adapter.out.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@Import(SearchCountJdbcRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.liquibase.enabled=false",
        "spring.sql.init.mode=never"
})
class SearchCountJdbcRepositoryTest extends AbstractOracleJdbcRepositoryTest {

    @Autowired
    private SearchCountJdbcRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        ensureSearchCountsTable();
        jdbcTemplate.update("delete from search_counts");
    }

    @Test
    void shouldInsertCountAsOneOnFirstIncrement() {
        repository.increment("fingerprint-1");

        Long total = jdbcTemplate.queryForObject(
                "select total_count from search_counts where fingerprint = ?",
                Long.class,
                "fingerprint-1"
        );

        assertEquals(1L, total);
    }

    @Test
    void shouldIncrementExistingCount() {
        repository.increment("fingerprint-1");
        repository.increment("fingerprint-1");

        long count = repository.getByFingerprint("fingerprint-1");

        assertEquals(2L, count);
    }

    @Test
    void shouldThrowWhenFingerprintDoesNotExistWithCurrentImplementation() {
        assertThrows(
                EmptyResultDataAccessException.class,
                () -> repository.getByFingerprint("missing-fingerprint")
        );
    }

    private void ensureSearchCountsTable() {
        try {
            jdbcTemplate.execute("select 1 from search_counts where 1 = 0");
        } catch (DataAccessException ex) {
            jdbcTemplate.execute("""
                    create table search_counts (
                        fingerprint varchar2(128) primary key,
                        total_count number(19,0) not null
                    )
                    """);
        }
    }
}