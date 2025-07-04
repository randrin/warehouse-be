package com.warehouse.bear.management.configuration;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WarehouseSwaggerConfiguration {

    @Bean
    public OpenAPI warehouseOpenAPI() {
        return new OpenAPI()
                // 1. Informazioni API
                .info(new Info()
                        .title("Warehouse Management System")
                        .description("Microservices Warehouse Management System")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("IT Software Vigevano")
                                .url("https://randrin-nzeukang.netlify.app/#about_me")
                                .email("warehouse@gmail.com"))
                        .license(new License()
                                .name("Terms of Use Licence")
                                .url("https://github.com/randrin/warehouse-be/blob/master/README.md")))
                // 2. Abilita sicurezza JWT
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                // 3. Definisce lo schema API Key per JWT
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(WarehouseUserConstants.WAREHOUSE_AUTHORIZATION)));
    }
}
