package com.Clinica.Practica01.core.exception;

import org.springframework.http.HttpStatus;

/** Violacion de una regla de negocio. Lleva el status HTTP a devolver (400/409...). */
public class BusinessException extends RuntimeException {
    private final HttpStatus status;

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BusinessException(String message) {
        this(message, HttpStatus.CONFLICT);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
