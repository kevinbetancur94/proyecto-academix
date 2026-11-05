package com.cesde.proyecto_academix.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

/**
 * Los campos pueden venir en null porque el PUT actualiza solo lo enviado (actualización parcial);
 * el servicio exige estudianteId y materiaId al crear. Si una nota viene, debe estar entre 0 y 5.
 */
public record CalificacionRequestDTO(
        Long estudianteId,
        Long materiaId,

        @DecimalMin(value = "0.0", message = "La actividad 1 no puede ser menor a 0")
        @DecimalMax(value = "5.0", message = "La actividad 1 no puede ser mayor a 5")
        Double act1,

        @DecimalMin(value = "0.0", message = "La actividad 2 no puede ser menor a 0")
        @DecimalMax(value = "5.0", message = "La actividad 2 no puede ser mayor a 5")
        Double act2,

        @DecimalMin(value = "0.0", message = "La actividad 3 no puede ser menor a 0")
        @DecimalMax(value = "5.0", message = "La actividad 3 no puede ser mayor a 5")
        Double act3,

        @Size(max = 500, message = "La observación no puede superar 500 caracteres")
        String observacion
) {
}
