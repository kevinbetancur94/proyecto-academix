package com.cesde.proyecto_academix.dto;

/** DTO de Entrada para Calificacion. Estudiante y materia se referencian solo por ID. */
public record CalificacionRequestDTO(
        Long estudianteId,
        Long materiaId,
        Double act1,
        Double act2,
        Double act3,
        String observacion
) {
}
