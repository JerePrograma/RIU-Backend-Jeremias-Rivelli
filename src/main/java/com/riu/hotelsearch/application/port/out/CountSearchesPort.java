package com.riu.hotelsearch.application.port.out;

public interface CountSearchesPort {
    long countByFingerprint(String fingerprint);
}