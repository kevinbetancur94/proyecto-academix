package com.cesde.proyecto_academix.exception;

/** Se lanza cuando un registro no existe en la BD. El handler global la convierte en HTTP 404. */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
