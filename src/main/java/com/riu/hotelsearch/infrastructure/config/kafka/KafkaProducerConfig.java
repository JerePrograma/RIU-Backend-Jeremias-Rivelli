package com.riu.hotelsearch.infrastructure.config.kafka;

import com.riu.hotelsearch.domain.port.out.PublishSearchEventPort;
import com.riu.hotelsearch.infrastructure.out.messaging.kafka.KafkaSearchProducer;
import com.riu.hotelsearch.infrastructure.out.messaging.kafka.SearchMessage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableConfigurationProperties(AppKafkaProperties.class)
public class KafkaProducerConfig {

    @Bean
    public PublishSearchEventPort publishSearchEventPort(
            KafkaTemplate<String, SearchMessage> kafkaTemplate,
            AppKafkaProperties kafkaProperties
    ) {
        return new KafkaSearchProducer(kafkaTemplate, kafkaProperties);
    }
}