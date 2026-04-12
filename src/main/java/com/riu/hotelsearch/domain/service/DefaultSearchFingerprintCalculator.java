package com.riu.hotelsearch.domain.service;

import com.riu.hotelsearch.domain.model.Search;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.stream.Collectors;

/**
 * Implementación por defecto del cálculo de fingerprint basada en SHA-256.
 *
 * <p>El valor se genera a partir de una concatenación determinística de los
 * campos relevantes de la búsqueda, respetando el orden original de las edades.</p>
 */
public class DefaultSearchFingerprintCalculator implements SearchFingerprintCalculator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Calcula el fingerprint determinístico de una búsqueda.
     *
     * @param search búsqueda validada
     * @return fingerprint en formato hexadecimal
     */
    @Override
    public String calculate(Search search) {
        String ages = search.ages().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String raw = String.join("|",
                search.hotelId(),
                search.checkIn().format(FORMATTER),
                search.checkOut().format(FORMATTER),
                ages
        );

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