package com.warehouse.bear.management.configuration;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class WarehouseSwaggerConfiguration {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfos())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .useDefaultResponseMessages(false)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.warehouse.bear.management"))
                .build();
    }

    private ApiInfo getApiInfos() {
        return new ApiInfo(
                "Warehouse Management System",
                "Microservices Warehouse Management System",
                "v1.0.0",
                "https://github.com/randrin/warehouse-be",
                new Contact(
                        "IT Software Vigevano",
                        "https://randrin-nzeukang.netlify.app/#about_me",
                        "warehouse@gmail.com"),
                "Terms of Use Licence",
                "https://github.com/randrin/warehouse-be/blob/master/README.md",
                Collections.emptyList());
    }

    private ApiKey apiKey() {
        return new ApiKey(
                "JWT", WarehouseUserConstants.WAREHOUSE_AUTHORIZATION,
                "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(
                "global",
                "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(
                "JWT",
                authorizationScopes));
    }
}
