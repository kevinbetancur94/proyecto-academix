package com.cesde.proyecto_academix.dto.portal;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record FilaCalificacionDTO(
        @NotNull(message = "El id del estudiante es obligatorio") Long id,
        List<Double> notas,
        String observacion) {
}
