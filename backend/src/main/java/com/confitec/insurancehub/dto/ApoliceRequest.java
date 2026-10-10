package com.confitec.insurancehub.dto;

import com.confitec.insurancehub.model.StatusApolice;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ApoliceRequest(
        @NotBlank(message = "Número é obrigatório")
        @Size(max = 50, message = "Número deve ter no máximo 50 caracteres")
        String numero,

        @NotBlank(message = "Tipo de seguro é obrigatório")
        @Size(max = 50, message = "Tipo de seguro deve ter no máximo 50 caracteres")
        String tipoSeguro,

        @NotNull(message = "Valor do prêmio é obrigatório")
        @Positive(message = "Valor do prêmio deve ser maior que zero")
        BigDecimal valorPremio,

        @NotNull(message = "Valor da cobertura é obrigatório")
        @Positive(message = "Valor da cobertura deve ser maior que zero")
        BigDecimal valorCobertura,

        @NotNull(message = "Data de início é obrigatória")
        LocalDate dataInicio,

        @NotNull(message = "Data de fim é obrigatória")
        LocalDate dataFim,

        @NotNull(message = "Status é obrigatório")
        StatusApolice status,

        @NotNull(message = "Cliente é obrigatório")
        Long clienteId) {
}
