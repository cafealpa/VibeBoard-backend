package com.chochocho.vibeboard.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("VibeBoard API")
                    .version("1.0")
                    .description("VibeBoard API Documentation")
                    .contact(
                        Contact()
                            .name("VibeBoard Team")
                            .email("contact@vibeboard.com")
                    )
                    .license(
                        License()
                            .name("MIT License")
                    )
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearer-jwt", 
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("Enter JWT token")
                    )
            )
    }
}