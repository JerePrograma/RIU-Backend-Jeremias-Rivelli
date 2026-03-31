package com.riu.hotelsearch.adapter.out.kafka;

import com.riu.hotelsearch.application.port.out.PublishSearchEventPort;
import com.riu.hotelsearch.config.kafka.AppKafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

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

    @Override
    public void publish(SearchMessage message) {
        kafkaTemplate.send(kafkaProperties.topic(), message.fingerprint(), message);
    }
}