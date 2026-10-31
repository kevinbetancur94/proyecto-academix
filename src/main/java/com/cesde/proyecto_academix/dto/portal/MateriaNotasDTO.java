package com.cesde.proyecto_academix.dto.portal;

import java.util.List;

/** Una materia con sus 3 notas: { "nombre": "Matemáticas", "notas": [3.5, 4.0, 4.4] }. */
public record MateriaNotasDTO(String nombre, List<Double> notas) {
}
