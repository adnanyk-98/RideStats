package com.ridestats.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rideStatsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RideStats API")
                        .version("1.0")
                        .description("Cycling analytics platform")
                        .contact(new Contact()
                                .name("Adnan Khambhati")));
    }
}