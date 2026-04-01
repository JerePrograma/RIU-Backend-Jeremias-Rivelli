package com.riu.hotelsearch.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KafkaTopicConfigTest {

    private final KafkaTopicConfig config = new KafkaTopicConfig();

    @Test
    void shouldCreateTopicFromProperties() {
        AppKafkaProperties properties = new AppKafkaProperties(
                "hotel_availability_searches",
                3,
                (short) 2,
                4
        );

        NewTopic topic = config.hotelAvailabilitySearchesTopic(properties);

        assertEquals("hotel_availability_searches", topic.name());
        assertEquals(3, topic.numPartitions());
        assertEquals(Short.valueOf((short) 2), topic.replicationFactor());
    }
}
