package com.cesde.proyecto_academix.dto.portal;

/** Resultado de guardar notas: cuántas filas se guardaron y cuántas se omitieron por estar incompletas. */
public record ResultadoGuardadoDTO(boolean ok, int guardados, int omitidos) {
}
