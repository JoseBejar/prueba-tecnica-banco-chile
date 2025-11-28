package com.josebejar.evaluacion.infraestructura.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI usuariosApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Usuarios API - Prueba Técnica")
                        .description("API para gestión de usuarios con JWT")
                        .version("v1.0.0")
                        .license(new License().name("MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación técnica")
                        .url("https://github.com/JoseBejar/..."));
    }
}
