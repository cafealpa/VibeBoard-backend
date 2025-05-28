package com.chochocho.vibeboard.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Swagger/OpenAPI 문서화를 위한 설정 클래스
 * API 문서를 자동으로 생성하기 위한 설정을 제공합니다.
 */
@Configuration
class OpenAPIConfig {

    /**
     * OpenAPI 문서 설정을 정의하는 메소드
     * 
     * @return 구성된 OpenAPI 객체
     */
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
