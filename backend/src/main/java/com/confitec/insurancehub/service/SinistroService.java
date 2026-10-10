package com.confitec.insurancehub.service;

import com.confitec.insurancehub.dto.SinistroRequest;
import com.confitec.insurancehub.dto.SinistroResponse;
import com.confitec.insurancehub.exception.BusinessRuleException;
import com.confitec.insurancehub.exception.ResourceNotFoundException;
import com.confitec.insurancehub.model.Apolice;
import com.confitec.insurancehub.model.Sinistro;
import com.confitec.insurancehub.model.StatusApolice;
import com.confitec.insurancehub.repository.ApoliceRepository;
import com.confitec.insurancehub.repository.SinistroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SinistroService {

    private static final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final SinistroRepository sinistros;
    private final ApoliceRepository apolices;

    public SinistroService(SinistroRepository sinistros, ApoliceRepository apolices) {
        this.sinistros = sinistros;
        this.apolices = apolices;
    }

    @Transactional(readOnly = true)
    public List<SinistroResponse> listar(Long apoliceId) {
        List<Sinistro> lista = (apoliceId == null)
                ? sinistros.findAllByOrderByIdDesc()
                : sinistros.findByApoliceIdOrderByIdDesc(apoliceId);
        return lista.stream().map(SinistroResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public SinistroResponse buscar(Long id) {
        return SinistroResponse.de(obter(id));
    }

    @Transactional
    public SinistroResponse criar(SinistroRequest r) {
        Apolice apolice = buscarApolice(r.apoliceId());
        if (apolice.getStatus() != StatusApolice.ATIVA) {
            throw new BusinessRuleException("Só é possível abrir sinistro em apólice ATIVA");
        }
        validarRegras(r, apolice);
        return SinistroResponse.de(sinistros.save(preencher(new Sinistro(), r, apolice)));
    }

    @Transactional
    public SinistroResponse atualizar(Long id, SinistroRequest r) {
        Sinistro s = obter(id);
        Apolice apolice = buscarApolice(r.apoliceId());
        validarRegras(r, apolice);
        return SinistroResponse.de(sinistros.save(preencher(s, r, apolice)));
    }

    @Transactional
    public void excluir(Long id) {
        sinistros.delete(obter(id));
    }

    private Sinistro obter(Long id) {
        return sinistros.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sinistro não encontrado"));
    }

    private Apolice buscarApolice(Long id) {
        return apolices.findById(id)
                .orElseThrow(() -> new BusinessRuleException("A apólice informada não existe"));
    }

    private void validarRegras(SinistroRequest r, Apolice apolice) {
        if (r.dataOcorrencia().isAfter(LocalDate.now())) {
            throw new BusinessRuleException("A data de ocorrência não pode estar no futuro");
        }
        if (r.dataOcorrencia().isBefore(apolice.getDataInicio()) || r.dataOcorrencia().isAfter(apolice.getDataFim())) {
            throw new BusinessRuleException("A data de ocorrência está fora da vigência da apólice ("
                    + apolice.getDataInicio().format(BR) + " a " + apolice.getDataFim().format(BR) + ")");
        }
        if (r.valorSolicitado().compareTo(apolice.getValorCobertura()) > 0) {
            throw new BusinessRuleException("O valor solicitado excede a cobertura da apólice");
        }
    }

    private Sinistro preencher(Sinistro s, SinistroRequest r, Apolice apolice) {
        s.setDescricao(r.descricao().trim());
        s.setDataOcorrencia(r.dataOcorrencia());
        s.setValorSolicitado(r.valorSolicitado());
        s.setStatus(r.status());
        s.setApolice(apolice);
        return s;
    }
}
