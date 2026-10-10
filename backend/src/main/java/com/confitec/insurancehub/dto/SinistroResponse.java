package com.confitec.insurancehub.dto;

import com.confitec.insurancehub.model.Sinistro;
import com.confitec.insurancehub.model.StatusSinistro;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SinistroResponse(
        Long id,
        String descricao,
        LocalDate dataOcorrencia,
        BigDecimal valorSolicitado,
        StatusSinistro status,
        Long apoliceId,
        String apoliceNumero,
        String clienteNome) {

    public static SinistroResponse de(Sinistro s) {
        return new SinistroResponse(
                s.getId(), s.getDescricao(), s.getDataOcorrencia(), s.getValorSolicitado(), s.getStatus(),
                s.getApolice().getId(), s.getApolice().getNumero(), s.getApolice().getCliente().getNome());
    }
}
