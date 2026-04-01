package com.riu.hotelsearch.adapter.out.persistence;

import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.model.SearchRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@Import({SearchWriteJdbcRepository.class, SearchRowMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.liquibase.enabled=false",
        "spring.sql.init.mode=never"
})
class SearchWriteJdbcRepositoryTest extends AbstractOracleJdbcRepositoryTest {

    @Autowired
    private SearchWriteJdbcRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        ensureSearchesTable();
        jdbcTemplate.update("delete from searches");
    }

    @Test
    void shouldInsertNewSearch() {
        SearchRecord record = new SearchRecord(
                "search-id-1",
                new Search(
                        "1234aBc",
                        LocalDate.of(2023, 12, 29),
                        LocalDate.of(2023, 12, 31),
                        List.of(30, 29, 1, 3)
                ),
                "fingerprint-1",
                Instant.parse("2026-03-31T18:10:00Z")
        );

        boolean inserted = repository.saveIfAbsent(record);

        assertTrue(inserted);

        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from searches where search_id = ?",
                Integer.class,
                "search-id-1"
        );

        assertEquals(1, count);
    }

    @Test
    void shouldReturnFalseWhenSearchIdAlreadyExists() {
        SearchRecord record = new SearchRecord(
                "search-id-1",
                new Search(
                        "1234aBc",
                        LocalDate.of(2023, 12, 29),
                        LocalDate.of(2023, 12, 31),
                        List.of(30, 29, 1, 3)
                ),
                "fingerprint-1",
                Instant.parse("2026-03-31T18:10:00Z")
        );

        assertTrue(repository.saveIfAbsent(record));
        assertFalse(repository.saveIfAbsent(record));
    }

    @Test
    void shouldPersistAgesCsvPreservingOrder() {
        SearchRecord record = new SearchRecord(
                "search-id-2",
                new Search(
                        "1234aBc",
                        LocalDate.of(2023, 12, 29),
                        LocalDate.of(2023, 12, 31),
                        List.of(3, 29, 30, 1)
                ),
                "fingerprint-2",
                Instant.parse("2026-03-31T18:10:00Z")
        );

        repository.saveIfAbsent(record);

        String agesCsv = jdbcTemplate.queryForObject(
                "select ages_csv from searches where search_id = ?",
                String.class,
                "search-id-2"
        );

        assertEquals("3,29,30,1", agesCsv);
    }

    private void ensureSearchesTable() {
        try {
            jdbcTemplate.execute("select 1 from searches where 1 = 0");
        } catch (DataAccessException ex) {
            jdbcTemplate.execute("""
                    create table searches (
                        search_id varchar2(100) primary key,
                        hotel_id varchar2(100) not null,
                        check_in date not null,
                        check_out date not null,
                        ages_csv varchar2(4000) not null,
                        fingerprint varchar2(128) not null,
                        created_at timestamp not null
                    )
                    """);
        }
    }
}