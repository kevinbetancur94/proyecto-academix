package com.cesde.proyecto_academix.dto;

/** DTO de Salida para Materia. Aplana la relación con el docente (id y nombre) y omite las colecciones. */
public record MateriaResponseDTO(
        Long id,
        String nombre,
        String codigo,
        Long docenteId,
        String docenteNombre,
        Boolean estadoActivo
) {
}
