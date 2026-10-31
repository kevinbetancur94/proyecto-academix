package com.cesde.proyecto_academix.dto.portal;

/** Lo que envía la pantalla de Login: correo (en el campo "usuario"), contraseña y rol elegido. */
public record LoginRequestDTO(String usuario, String password, String rol) {
}
