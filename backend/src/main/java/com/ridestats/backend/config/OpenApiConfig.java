package com.ridestats.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rideStatsOpenAPI() {
        return new OpenAPI()
            .components(new Components().addSecuritySchemes("basicAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("basic")))
            .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .info(new Info()
                        .title("RideStats API")
                        .version("1.0")
                        .description("Cycling analytics platform")
                        .contact(new Contact()
                                .name("Adnan Khambhati")));
    }
}