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
                        .description("""
                                API para registrar búsquedas de disponibilidad de hotel de forma asíncrona
                                y consultar cuántas veces se repitió una búsqueda equivalente.
                                
                                La operación POST /search responde 202 Accepted porque la persistencia se
                                desacopla mediante Kafka.
                                
                                El orden de la lista de edades forma parte de la identidad de la búsqueda.
                                """))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación del proyecto")
                        .url("REEMPLAZAR_POR_URL_DEL_REPOSITORIO"));
    }
}