package com.riu.hotelsearch.adapter.out.kafka;

import com.riu.hotelsearch.application.port.out.PublishSearchEventPort;
import com.riu.hotelsearch.config.kafka.AppKafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Productor encargado de publicar eventos de búsqueda en Kafka.
 *
 * <p>La clave del mensaje es el fingerprint para conservar afinidad por partición
 * entre búsquedas equivalentes.</p>
 */
@Component
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

    /**
     * Publica un evento de búsqueda en el tópico configurado.
     *
     * @param message mensaje a publicar
     */
    @Override
    public void publish(SearchMessage message) {
        kafkaTemplate.send(kafkaProperties.topic(), message.fingerprint(), message);
    }
}