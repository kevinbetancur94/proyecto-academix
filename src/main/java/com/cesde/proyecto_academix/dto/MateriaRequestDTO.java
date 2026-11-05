package com.cesde.proyecto_academix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MateriaRequestDTO(
        @NotBlank(message = "El nombre de la materia es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @NotBlank(message = "El código de la materia es obligatorio")
        @Size(max = 20, message = "El código no puede superar 20 caracteres")
        String codigo,

        @NotNull(message = "El docente es obligatorio")
        Long docenteId
) {
}
