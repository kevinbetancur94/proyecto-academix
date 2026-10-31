package com.cesde.proyecto_academix.dto.portal;

import java.util.List;

/** Botón "Guardar" del panel docente. materiaId llega como texto porque viene de un <select>. */
public record GuardarCalificacionesDTO(String materiaId, List<FilaCalificacionDTO> calificaciones) {
}
