package com.cesde.proyecto_academix.dto.portal;

import java.time.LocalDate;

/**
 * Tarea en el formato del calendario de React: la materia va por NOMBRE y el tipo
 * con su texto bonito ("Investigación"), no con el enum (INVESTIGACION).
 */
public record TareaCalendarioDTO(
        Long id,
        String titulo,
        String descripcion,
        String materia,
        String tipo,
        String grado,
        LocalDate fecha,
        String observaciones
) {
}
