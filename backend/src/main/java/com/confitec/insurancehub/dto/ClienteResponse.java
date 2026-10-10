package com.confitec.insurancehub.dto;

import com.confitec.insurancehub.model.Cliente;

public record ClienteResponse(Long id, String nome, String email, String cpf) {

    public static ClienteResponse de(Cliente c) {
        return new ClienteResponse(c.getId(), c.getNome(), c.getEmail(), c.getCpf());
    }
}
