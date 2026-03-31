package com.riu.hotelsearch.adapter.out.persistence;

import com.riu.hotelsearch.domain.model.Search;
import com.riu.hotelsearch.domain.model.SearchRecord;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Component
public class SearchRowMapper implements RowMapper<SearchRecord> {

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