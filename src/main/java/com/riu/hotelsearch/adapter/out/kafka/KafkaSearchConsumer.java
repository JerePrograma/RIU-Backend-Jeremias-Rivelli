package com.riu.hotelsearch.adapter.out.kafka;

import com.riu.hotelsearch.application.port.in.PersistSearchUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Consumidor encargado de recibir búsquedas desde Kafka y delegar su persistencia.
 *
 * <p>El offset se confirma manualmente solo después de una persistencia exitosa.</p>
 */
@Component
public class KafkaSearchConsumer {

    private final PersistSearchUseCase persistSearchUseCase;

    public KafkaSearchConsumer(PersistSearchUseCase persistSearchUseCase) {
        this.persistSearchUseCase = persistSearchUseCase;
    }

    /**
     * Procesa un mensaje consumido desde Kafka y confirma su offset
     * una vez completada la persistencia.
     *
     * @param message mensaje recibido
     * @param acknowledgment confirmación manual del offset
     */
    @KafkaListener(
            topics = "${app.kafka.topic:hotel_availability_searches}",
            containerFactory = "searchKafkaListenerContainerFactory"
    )
    public void consume(SearchMessage message, Acknowledgment acknowledgment) {
        persistSearchUseCase.persist(message);
        acknowledgment.acknowledge();
    }
}