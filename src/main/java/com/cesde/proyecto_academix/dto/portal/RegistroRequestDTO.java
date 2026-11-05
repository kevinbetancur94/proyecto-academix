package com.cesde.proyecto_academix.dto.portal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido") String correo,
        String telefono,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password,
        @NotBlank(message = "Debe confirmar la contraseña") String confirmar,
        @NotBlank(message = "El rol es obligatorio") String rol,
        String grado,
        String departamento,
        String ciudad
) {
}
