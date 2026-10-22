package com.cesde.proyecto_academix.dto;

import java.time.LocalDate;

import com.cesde.proyecto_academix.model.enums.TipoTarea;

/** DTO de Salida para Tarea. Aplana la relación con Materia (id y nombre). */
public record TareaResponseDTO(
        Long id,
        String titulo,
        String descripcion,
        Long materiaId,
        String materiaNombre,
        TipoTarea tipo,
        String grado,
        LocalDate fecha,
        String observaciones,
        Boolean estadoActivo
) {
}
