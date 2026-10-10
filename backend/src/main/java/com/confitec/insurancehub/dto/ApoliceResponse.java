package com.confitec.insurancehub.dto;

import com.confitec.insurancehub.model.Apolice;
import com.confitec.insurancehub.model.StatusApolice;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ApoliceResponse(
        Long id,
        String numero,
        String tipoSeguro,
        BigDecimal valorPremio,
        BigDecimal valorCobertura,
        LocalDate dataInicio,
        LocalDate dataFim,
        StatusApolice status,
        Long clienteId,
        String clienteNome) {

    public static ApoliceResponse de(Apolice a) {
        return new ApoliceResponse(
                a.getId(), a.getNumero(), a.getTipoSeguro(), a.getValorPremio(), a.getValorCobertura(),
                a.getDataInicio(), a.getDataFim(), a.getStatus(),
                a.getCliente().getId(), a.getCliente().getNome());
    }
}
