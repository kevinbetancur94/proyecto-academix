package com.cesde.proyecto_academix.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS: le dice al navegador qué páginas web pueden llamar a esta API.
 * Sin esto, el frontend (otra dirección/puerto) recibe "blocked by CORS policy".
 *
 * Por defecto acepta cualquier origen (*). Para restringirlo, en Render se crea la
 * variable CORS_ORIGENES con las direcciones separadas por coma, por ejemplo:
 *   http://localhost:5173,https://academix-front.onrender.com
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${CORS_ORIGENES:*}")
    private String[] origenesPermitidos;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(origenesPermitidos)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
