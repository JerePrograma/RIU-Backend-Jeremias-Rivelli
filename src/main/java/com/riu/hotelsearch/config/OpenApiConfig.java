package com.riu.hotelsearch.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Search API")
                        .version("1.0.0")
                        .description("API para registrar búsquedas de disponibilidad y consultar su conteo"))
                .externalDocs(new ExternalDocumentation()
                        .description("README")
                        .url("http://localhost:8080"));
    }
}