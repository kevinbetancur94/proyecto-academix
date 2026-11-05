package com.cesde.proyecto_academix.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Metadatos de la documentación interactiva (Swagger UI en /swagger-ui.html y Scalar en /scalar). */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI academixOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Academix API")
                .version("1.0.0")
                .description("API REST de Academix, portal académico que conecta a estudiantes, acudientes y docentes: "
                        + "gestión de usuarios, materias, tareas, calificaciones y boletines académicos.")
                .contact(new Contact().name("Equipo Academix - CESDE")));
    }
}
