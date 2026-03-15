package com.fleetpulse.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fleetPulseOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("FleetPulse API")
                        .description("Fleet Operations & Predictive Maintenance Intelligence Platform — REST API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("FleetPulse Engineering")
                                .url("https://sankartk.dev"))
                        .license(new License().name("MIT")));
    }
}
