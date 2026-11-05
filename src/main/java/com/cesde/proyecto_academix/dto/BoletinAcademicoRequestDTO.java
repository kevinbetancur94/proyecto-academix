package com.cesde.proyecto_academix.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BoletinAcademicoRequestDTO(
        @NotNull(message = "El estudiante es obligatorio")
        Long estudianteId,

        @NotNull(message = "El promedio general es obligatorio")
        @DecimalMin(value = "0.0", message = "El promedio no puede ser menor a 0")
        @DecimalMax(value = "5.0", message = "El promedio no puede ser mayor a 5")
        Double promedioGeneral,

        @Min(value = 0, message = "Las materias aprobadas no pueden ser negativas")
        Integer materiasAprobadas,

        @Min(value = 0, message = "Las materias en riesgo no pueden ser negativas")
        Integer materiasEnRiesgo
) {
}
