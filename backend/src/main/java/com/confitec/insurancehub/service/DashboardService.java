package com.confitec.insurancehub.service;

import com.confitec.insurancehub.dto.DashboardResponse;
import com.confitec.insurancehub.model.StatusApolice;
import com.confitec.insurancehub.model.StatusSinistro;
import com.confitec.insurancehub.repository.ApoliceRepository;
import com.confitec.insurancehub.repository.ClienteRepository;
import com.confitec.insurancehub.repository.SinistroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private static final List<StatusSinistro> EM_ANDAMENTO =
            List.of(StatusSinistro.ABERTO, StatusSinistro.EM_ANALISE);

    private final ClienteRepository clientes;
    private final ApoliceRepository apolices;
    private final SinistroRepository sinistros;

    public DashboardService(ClienteRepository clientes, ApoliceRepository apolices, SinistroRepository sinistros) {
        this.clientes = clientes;
        this.apolices = apolices;
        this.sinistros = sinistros;
    }

    @Transactional(readOnly = true)
    public DashboardResponse resumo() {
        return new DashboardResponse(
                clientes.count(),
                apolices.countByStatus(StatusApolice.ATIVA),
                sinistros.countByStatusIn(EM_ANDAMENTO),
                zeroSeNulo(apolices.somarPremios(StatusApolice.ATIVA)),
                zeroSeNulo(apolices.somarCoberturas(StatusApolice.ATIVA)),
                zeroSeNulo(sinistros.somarValores(EM_ANDAMENTO)));
    }

    /** SUM() devolve null quando não há linhas. */
    private BigDecimal zeroSeNulo(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
