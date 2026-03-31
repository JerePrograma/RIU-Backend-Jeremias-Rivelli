package com.riu.hotelsearch.application.support;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidSearchIdGenerator implements SearchIdGenerator {

    @Override
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}