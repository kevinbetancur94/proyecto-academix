package com.cesde.proyecto_academix.dto;

import java.time.LocalDate;

import com.cesde.proyecto_academix.model.enums.TipoTarea;

/** DTO de Entrada para Tarea. La materia se referencia solo por su ID. */
public record TareaRequestDTO(
        String titulo,
        String descripcion,
        Long materiaId,
        TipoTarea tipo,
        String grado,
        LocalDate fecha,
        String observaciones
) {
}
