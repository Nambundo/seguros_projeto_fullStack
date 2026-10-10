package com.confitec.insurancehub.exception;

/** 409 - conflito com o estado atual (duplicidade, registro em uso). */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
}
