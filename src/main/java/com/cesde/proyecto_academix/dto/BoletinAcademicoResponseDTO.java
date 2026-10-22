package com.cesde.proyecto_academix.dto;

/** DTO de Salida para BoletinAcademico. Aplana la relación con el estudiante (id y nombre). */
public record BoletinAcademicoResponseDTO(
        Long id,
        Long estudianteId,
        String estudianteNombre,
        Double promedioGeneral,
        Integer materiasAprobadas,
        Integer materiasEnRiesgo
) {
}
