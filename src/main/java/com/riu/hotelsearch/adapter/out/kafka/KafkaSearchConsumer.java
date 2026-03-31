package com.riu.hotelsearch.adapter.out.kafka;

import com.riu.hotelsearch.application.port.in.PersistSearchUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchConsumer {

    private final PersistSearchUseCase persistSearchUseCase;

    public KafkaSearchConsumer(PersistSearchUseCase persistSearchUseCase) {
        this.persistSearchUseCase = persistSearchUseCase;
    }

    @KafkaListener(
            topics = "${app.kafka.topic:hotel_availability_searches}",
            containerFactory = "searchKafkaListenerContainerFactory"
    )
    public void consume(SearchMessage message, Acknowledgment acknowledgment) {
        persistSearchUseCase.persist(message);
        acknowledgment.acknowledge();
    }
}