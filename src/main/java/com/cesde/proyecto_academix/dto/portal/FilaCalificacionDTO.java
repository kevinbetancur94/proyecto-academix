package com.cesde.proyecto_academix.dto.portal;

import java.util.List;

/** Una fila que el docente guarda: id del estudiante, sus 3 notas y la observación. */
public record FilaCalificacionDTO(Long id, List<Double> notas, String observacion) {
}
