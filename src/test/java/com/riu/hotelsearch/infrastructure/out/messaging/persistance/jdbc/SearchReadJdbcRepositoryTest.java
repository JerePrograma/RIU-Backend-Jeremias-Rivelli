package com.riu.hotelsearch.infrastructure.out.messaging.persistance.jdbc;

import com.riu.hotelsearch.domain.model.SearchRecord;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchReadJdbcRepository;
import com.riu.hotelsearch.infrastructure.persistence.jdbc.SearchRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@Import({SearchReadJdbcRepository.class, SearchRowMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.liquibase.enabled=false",
        "spring.sql.init.mode=never"
})
class SearchReadJdbcRepositoryTest extends AbstractOracleJdbcRepositoryTest {

    @Autowired
    private SearchReadJdbcRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        ensureSearchesTable();
        jdbcTemplate.update("delete from searches");
    }

    @Test
    void shouldFindSearchById() {
        jdbcTemplate.update(
                """
                insert into searches (search_id, hotel_id, check_in, check_out, ages_csv, fingerprint, created_at)
                values (?, ?, ?, ?, ?, ?, ?)
                """,
                "search-id-1",
                "1234aBc",
                java.sql.Date.valueOf(LocalDate.of(2023, 12, 29)),
                java.sql.Date.valueOf(LocalDate.of(2023, 12, 31)),
                "30,29,1,3",
                "fingerprint-1",
                Timestamp.from(Instant.parse("2026-03-31T18:10:00Z"))
        );

        Optional<SearchRecord> result = repository.findById("search-id-1");

        assertTrue(result.isPresent());
        assertEquals("search-id-1", result.get().searchId());
        assertEquals("1234aBc", result.get().search().hotelId());
        assertEquals(LocalDate.of(2023, 12, 29), result.get().search().checkIn());
        assertEquals(LocalDate.of(2023, 12, 31), result.get().search().checkOut());
        assertEquals("fingerprint-1", result.get().fingerprint());
        assertEquals(List.of(30, 29, 1, 3), result.get().search().ages());
    }

    @Test
    void shouldReturnEmptyWhenSearchDoesNotExist() {
        Optional<SearchRecord> result = repository.findById("missing-id");

        assertTrue(result.isEmpty());
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