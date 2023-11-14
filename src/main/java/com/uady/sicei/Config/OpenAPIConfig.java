package com.uady.sicei.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API Rest AWS")
                .version("v0.0.1")
                .description("Primera entrega proyecto AWS")
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.proyectoAWSRest.com/")
                )
        );
    }
}
