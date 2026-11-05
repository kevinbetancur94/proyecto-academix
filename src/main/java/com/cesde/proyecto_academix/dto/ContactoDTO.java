package com.cesde.proyecto_academix.dto;

import jakarta.validation.constraints.Size;

/** DTO anidado para los datos de contacto (reemplaza al @Embeddable Contacto en la API). */
public record ContactoDTO(
        @Size(max = 20, message = "El teléfono no puede superar 20 caracteres")
        String telefono,
        String departamento,
        String ciudad
) {
}
