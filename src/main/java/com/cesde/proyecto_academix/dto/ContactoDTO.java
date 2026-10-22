package com.cesde.proyecto_academix.dto;

/** DTO anidado para los datos de contacto (reemplaza al @Embeddable Contacto en la API). */
public record ContactoDTO(
        String telefono,
        String departamento,
        String ciudad
) {
}
