package com.warehouse.bear.management.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WarehouseOpenApiConfiguration {

    @Bean
    public OpenAPI WarehouseOpenApiConfiguration() {
        return new OpenAPI()
                .info(new Info()
                        .title("Warehouse API")
                        .version("1.0")
                        .description("Documentazione delle API del sistema Warehouse"));
    }
}
