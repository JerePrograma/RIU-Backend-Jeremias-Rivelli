package com.riu.hotelsearch;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class HotelSearchApplicationTest {

    @Test
    void shouldRunSpringApplication() {
        String[] args = {"--spring.main.web-application-type=none"};

        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            HotelSearchApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(HotelSearchApplication.class, args));
        }
    }
}