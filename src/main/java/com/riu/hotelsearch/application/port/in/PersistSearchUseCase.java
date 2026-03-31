package com.riu.hotelsearch.application.port.in;

import com.riu.hotelsearch.adapter.out.kafka.SearchMessage;

public interface PersistSearchUseCase {
    void persist(SearchMessage message);
}