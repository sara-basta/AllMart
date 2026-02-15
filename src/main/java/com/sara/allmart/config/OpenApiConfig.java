package com.sara.allmart.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI allmartOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AllMart API")
                        .description("Backend API for the AllMart application. Supports Products, Orders, Users, and Cart management.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Sara Basta")
                                .email("saraabastaa@gmail.com")
                                .url("https://github.com/sara-basta")
                        )
                )
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"));
        
    }
}