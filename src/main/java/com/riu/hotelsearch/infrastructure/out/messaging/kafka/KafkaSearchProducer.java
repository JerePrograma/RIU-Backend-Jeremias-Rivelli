package com.riu.hotelsearch.infrastructure.out.messaging.kafka;

import com.riu.hotelsearch.application.exception.SearchPublicationException;
import com.riu.hotelsearch.domain.event.SearchRegisteredEvent;
import com.riu.hotelsearch.domain.port.out.PublishSearchEventPort;
import com.riu.hotelsearch.infrastructure.config.kafka.AppKafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Productor encargado de publicar eventos de búsqueda en Kafka.
 *
 * <p>La clave del mensaje es el fingerprint para conservar afinidad por partición
 * entre búsquedas equivalentes.</p>
 */
public class KafkaSearchProducer implements PublishSearchEventPort {

    private final KafkaTemplate<String, SearchMessage> kafkaTemplate;
    private final AppKafkaProperties kafkaProperties;

    public KafkaSearchProducer(
            KafkaTemplate<String, SearchMessage> kafkaTemplate,
            AppKafkaProperties kafkaProperties
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProperties = kafkaProperties;
    }

    @Override
    public void publish(SearchRegisteredEvent event) {
        SearchMessage message = toMessage(event);

        try {
            kafkaTemplate
                    .send(kafkaProperties.topic(), message.fingerprint(), message)
                    .get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SearchPublicationException("Interrupted while publishing search event", e);
        } catch (ExecutionException | TimeoutException e) {
            throw new SearchPublicationException("Could not publish search event to Kafka", e);
        }
    }

    private SearchMessage toMessage(SearchRegisteredEvent event) {
        return new SearchMessage(
                event.searchId(),
                event.fingerprint(),
                event.hotelId(),
                event.checkIn(),
                event.checkOut(),
                event.ages()
        );
    }
}