package com.cesde.proyecto_academix.dto;

import java.time.LocalDate;

import com.cesde.proyecto_academix.model.enums.TipoTarea;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TareaRequestDTO(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede superar 150 caracteres")
        String titulo,

        @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
        String descripcion,

        @NotNull(message = "La materia es obligatoria")
        Long materiaId,

        @NotNull(message = "El tipo de tarea es obligatorio")
        TipoTarea tipo,

        String grado,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @Size(max = 500, message = "Las observaciones no pueden superar 500 caracteres")
        String observaciones
) {
}
