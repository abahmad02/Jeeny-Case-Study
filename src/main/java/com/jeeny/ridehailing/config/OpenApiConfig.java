package com.jeeny.ridehailing.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) configuration for API documentation
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Mini Ride Booking System API",
        version = "1.0.0",
        description = """
            **Backend API for a mini ride-hailing application**
            
            ## How to use this API:
            1. **Register** a user as PASSENGER or DRIVER
            2. **Login** to get a JWT token
            3. **Authorize** by clicking 🔒 and entering: `Bearer YOUR_TOKEN`
            4. **Test endpoints** - most require authentication
            
            ## Sample Users (password: password123):
            - **Passengers**: john.doe@example.com, jane.smith@example.com, alice.johnson@example.com
            - **Drivers**: bob.wilson@example.com, carol.brown@example.com, david.lee@example.com
            """,
        contact = @Contact(
            name = "Jeeny Ride Hailing",
            email = "support@jeeny.com",
            url = "https://jeeny.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            description = "Local Development Server",
            url = "http://localhost:8080"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "JWT Authorization header using the Bearer scheme. Enter: Bearer YOUR_JWT_TOKEN"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token with 'Bearer ' prefix")));
    }
}
