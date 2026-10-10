package com.confitec.insurancehub.exception;

/** 422 - dados válidos no formato, mas que violam uma regra de negócio. */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) { super(message); }
}
