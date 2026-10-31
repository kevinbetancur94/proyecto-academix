package com.cesde.proyecto_academix.dto.portal;

import java.util.List;

/**
 * Un estudiante con sus notas por materia. Lo usan las vistas "Mis notas" (estudiante)
 * y "Mis hijos" / "Detalle" (acudiente). acudienteId le permite a React comprobar
 * que el acudiente solo vea a sus propios hijos.
 */
public record HijoDTO(Long id, Long acudienteId, String nombre, String grado, List<MateriaNotasDTO> materias) {
}
