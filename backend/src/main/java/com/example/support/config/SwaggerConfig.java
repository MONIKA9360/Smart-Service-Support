package com.example.support.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Springdoc OpenAPI / Swagger UI configuration.
 * Accessible at: /swagger-ui/index.html
 */
@Configuration
public class SwaggerConfig {

    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Smart Service & Support Management System API")
                .version("1.0.0")
                .description(
                    "REST API for the Smart Service & Support Management System.\n\n" +
                    "**Roles:** ROLE_ADMIN | ROLE_AGENT | ROLE_CUSTOMER\n\n" +
                    "**Authentication:** JWT Bearer token (obtained via POST /api/auth/login)"
                )
                .contact(new Contact()
                    .name("Smart Support Team")
                    .email("admin@smartsupport.com"))
                .license(new License()
                    .name("MIT")
                    .url("https://opensource.org/licenses/MIT"))
            )
            .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
            .components(new Components()
                .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                    .name(BEARER_AUTH)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Enter JWT token obtained from /api/auth/login")
                )
            );
    }
}
