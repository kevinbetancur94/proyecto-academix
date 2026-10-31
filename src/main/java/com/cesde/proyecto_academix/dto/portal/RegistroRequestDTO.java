package com.cesde.proyecto_academix.dto.portal;

/** Formulario de la pantalla Registro, tal como lo envía React. */
public record RegistroRequestDTO(
        String nombre,
        String correo,
        String telefono,
        String password,
        String confirmar,
        String rol,
        String grado,
        String departamento,
        String ciudad
) {
}
