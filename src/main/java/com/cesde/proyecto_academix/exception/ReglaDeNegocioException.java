package com.cesde.proyecto_academix.exception;

/** Se lanza cuando se viola una regla de negocio. El handler global la convierte en HTTP 400. */
public class ReglaDeNegocioException extends RuntimeException {

    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }
}
