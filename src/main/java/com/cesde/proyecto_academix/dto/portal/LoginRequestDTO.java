package com.cesde.proyecto_academix.dto.portal;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "El usuario es obligatorio") String usuario,
        @NotBlank(message = "La contraseña es obligatoria") String password,
        @NotBlank(message = "El rol es obligatorio") String rol) {
}
