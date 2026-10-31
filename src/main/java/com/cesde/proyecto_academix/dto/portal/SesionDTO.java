package com.cesde.proyecto_academix.dto.portal;

/**
 * Lo que el frontend guarda como "sesión" después del login.
 * rol va en minúscula ("estudiante", "docente", "acudiente") porque así lo usa React.
 * estudianteId solo tiene valor cuando la cuenta es de un estudiante.
 */
public record SesionDTO(Long id, String nombre, String correo, String rol, String grado, Long estudianteId) {
}
