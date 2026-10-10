package com.confitec.insurancehub.dto;

import com.confitec.insurancehub.model.StatusSinistro;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SinistroRequest(
        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        String descricao,

        @NotNull(message = "Data de ocorrência é obrigatória")
        LocalDate dataOcorrencia,

        @NotNull(message = "Valor solicitado é obrigatório")
        @Positive(message = "Valor solicitado deve ser maior que zero")
        BigDecimal valorSolicitado,

        @NotNull(message = "Status é obrigatório")
        StatusSinistro status,

        @NotNull(message = "Apólice é obrigatória")
        Long apoliceId) {
}
