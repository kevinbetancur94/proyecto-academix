package com.cesde.proyecto_academix.dto.portal;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GuardarCalificacionesDTO(
        @NotBlank(message = "La materia es obligatoria") String materiaId,
        @NotNull(message = "Debe enviar la lista de calificaciones") @Valid List<FilaCalificacionDTO> calificaciones) {
}
