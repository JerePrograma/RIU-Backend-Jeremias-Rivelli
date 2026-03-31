package com.riu.hotelsearch.adapter.out.kafka;

import com.riu.hotelsearch.application.port.out.PublishSearchEventPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchProducer implements PublishSearchEventPort {

    private static final String TOPIC = "hotel_availability_searches";

    private final KafkaTemplate<String, SearchMessage> kafkaTemplate;

    public KafkaSearchProducer(KafkaTemplate<String, SearchMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(SearchMessage message) {
        kafkaTemplate.send(TOPIC, message.searchId(), message);
    }
}