package com.cesde.proyecto_academix.dto;

import java.time.LocalDateTime;

import com.cesde.proyecto_academix.model.enums.RolUsuario;

/**
 * DTO de Salida para Usuario.
 * IMPORTANTE: NO incluye 'password' (dato sensible) ni las colecciones
 * 'estudiantesACargo', 'acudientes' y 'materiasDictadas', que causaban recursión infinita en el JSON.
 */
public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String correo,
        RolUsuario rol,
        String grado,
        ContactoDTO contacto,
        Boolean estadoActivo,
        LocalDateTime fechaCreacion
) {
}
