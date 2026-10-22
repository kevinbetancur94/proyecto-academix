package com.cesde.proyecto_academix.dto;

import com.cesde.proyecto_academix.model.enums.RolUsuario;

/**
 * DTO de Entrada para Usuario.
 * No incluye 'id' ni los campos de auditoría (fechaCreacion, estadoActivo...), que maneja el sistema.
 */
public record UsuarioRequestDTO(
        String nombre,
        String correo,
        String password,
        RolUsuario rol,
        String grado,
        ContactoDTO contacto
) {
}
