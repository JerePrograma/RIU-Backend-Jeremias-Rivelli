package com.riu.hotelsearch.infrastructure.config.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class VirtualThreadsConfig {

    @Bean(destroyMethod = "close")
    public AsyncTaskExecutor kafkaListenerTaskExecutor(AppKafkaProperties properties) {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("kafka-vt-");
        executor.setVirtualThreads(true);
        executor.setConcurrencyLimit(properties.listenerConcurrency());
        executor.setTaskTerminationTimeout(30_000);
        return executor;
    }
}