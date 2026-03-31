package com.riu.hotelsearch.domain.service;

import com.riu.hotelsearch.domain.model.Search;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.stream.Collectors;

@Component
public class DefaultSearchFingerprintCalculator implements SearchFingerprintCalculator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public String calculate(Search search) {
        String ages = search.ages().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String raw = new StringBuilder()
                .append(search.hotelId()).append('|')
                .append(search.checkIn().format(FORMATTER)).append('|')
                .append(search.checkOut().format(FORMATTER)).append('|')
                .append(ages)
                .toString();

        return sha256(raw);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (Exception e) {
            throw new IllegalStateException("Could not calculate fingerprint", e);
        }
    }
}