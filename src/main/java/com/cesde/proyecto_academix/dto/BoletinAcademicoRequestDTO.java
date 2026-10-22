package com.cesde.proyecto_academix.dto;

/** DTO de Entrada para BoletinAcademico. El estudiante se referencia solo por su ID. */
public record BoletinAcademicoRequestDTO(
        Long estudianteId,
        Double promedioGeneral,
        Integer materiasAprobadas,
        Integer materiasEnRiesgo
) {
}
