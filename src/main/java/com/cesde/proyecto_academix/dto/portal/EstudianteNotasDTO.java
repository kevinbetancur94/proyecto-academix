package com.cesde.proyecto_academix.dto.portal;

import java.util.List;

/** Fila de la tabla del panel docente. notas puede traer null cuando todavía no hay nota. */
public record EstudianteNotasDTO(Long id, String nombre, List<Double> notas, String observacion) {
}
