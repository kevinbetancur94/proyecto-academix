package com.cesde.proyecto_academix.dto;

/** DTO de Entrada para Materia. El docente se referencia solo por su ID. */
public record MateriaRequestDTO(
        String nombre,
        String codigo,
        Long docenteId
) {
}
