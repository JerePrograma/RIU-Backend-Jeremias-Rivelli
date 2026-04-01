package com.riu.hotelsearch.application.support;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UuidSearchIdGeneratorTest {

    private final UuidSearchIdGenerator generator = new UuidSearchIdGenerator();

    @Test
    void shouldGenerateValidUuid() {
        String id = generator.nextId();

        assertNotNull(id);
        assertDoesNotThrow(() -> UUID.fromString(id));
    }

    @Test
    void shouldGenerateDifferentIdsOnConsecutiveCalls() {
        String id1 = generator.nextId();
        String id2 = generator.nextId();

        assertNotEquals(id1, id2);
    }
}
