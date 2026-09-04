package com.Clinica.Practica01.core.exception;

/** Recurso no encontrado (o borrado logicamente). Se traduce a HTTP 404. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
