package com.cesde.proyecto_academix.dto.portal;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

/**
 * Tarea en el formato del calendario de React: la materia va por NOMBRE y el tipo
 * con su texto bonito ("Investigación"), no con el enum (INVESTIGACION).
 */
public record TareaCalendarioDTO(
        Long id,
        @NotBlank(message = "El título es obligatorio") String titulo,
        String descripcion,
        String materia,
        String tipo,
        String grado,
        LocalDate fecha,
        String observaciones
) {
}
