package com.riu.hotelsearch.adapter.out.kafka;

import com.riu.hotelsearch.application.port.in.PersistSearchUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchConsumer {

    private final PersistSearchUseCase persistSearchUseCase;

    public KafkaSearchConsumer(PersistSearchUseCase persistSearchUseCase) {
        this.persistSearchUseCase = persistSearchUseCase;
    }

    @KafkaListener(topics = "hotel_availability_searches", groupId = "hotel-search-group")
    public void consume(SearchMessage message) {
        persistSearchUseCase.persist(message);
    }
}