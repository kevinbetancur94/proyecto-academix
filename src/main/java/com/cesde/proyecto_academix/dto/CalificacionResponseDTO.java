package com.cesde.proyecto_academix.dto;

/**
 * DTO de Salida para Calificacion.
 * 'promedio' es un campo calculado (no existe en la entidad): ejemplo de que el DTO
 * puede presentar los datos de forma distinta a como se guardan.
 */
public record CalificacionResponseDTO(
        Long id,
        Long estudianteId,
        String estudianteNombre,
        Long materiaId,
        String materiaNombre,
        Double act1,
        Double act2,
        Double act3,
        Double promedio,
        String observacion
) {
}
