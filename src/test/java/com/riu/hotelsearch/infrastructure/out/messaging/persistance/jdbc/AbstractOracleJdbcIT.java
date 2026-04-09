package com.riu.hotelsearch.infrastructure.out.messaging.persistance.jdbc;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

abstract class AbstractOracleJdbcRepositoryTest {

    private static final DockerImageName ORACLE_IMAGE =
            DockerImageName.parse("gvenzl/oracle-free:23-slim-faststart");

    static final OracleContainer ORACLE = new OracleContainer(ORACLE_IMAGE)
            .withStartupTimeout(Duration.ofMinutes(5));

    static {
        ORACLE.start();
    }

    @DynamicPropertySource
    static void configureOracleProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", ORACLE::getJdbcUrl);
        registry.add("spring.datasource.username", ORACLE::getUsername);
        registry.add("spring.datasource.password", ORACLE::getPassword);
        registry.add("spring.datasource.driver-class-name", ORACLE::getDriverClassName);
    }
}