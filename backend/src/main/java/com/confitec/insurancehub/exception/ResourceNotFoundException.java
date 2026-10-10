package com.confitec.insurancehub.exception;

/** 404 - recurso não existe. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
}
