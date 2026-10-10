package com.confitec.insurancehub.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClienteRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void aceitaCpfValidoComOuSemMascara() {
        assertTrue(validator.validate(new ClienteRequest("Maria", "maria@exemplo.com", "52998224725")).isEmpty());
        assertTrue(validator.validate(new ClienteRequest("Maria", "maria@exemplo.com", "529.982.247-25")).isEmpty());
    }

    @Test
    void rejeitaCpfInvalido() {
        assertFalse(validator.validate(new ClienteRequest("Maria", "maria@exemplo.com", "12345678900")).isEmpty());
    }

    @Test
    void rejeitaEmailInvalido() {
        assertFalse(validator.validate(new ClienteRequest("Maria", "isso-nao-e-email", "52998224725")).isEmpty());
    }
}
