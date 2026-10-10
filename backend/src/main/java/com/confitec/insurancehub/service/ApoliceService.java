package com.confitec.insurancehub.service;

import com.confitec.insurancehub.dto.ApoliceRequest;
import com.confitec.insurancehub.dto.ApoliceResponse;
import com.confitec.insurancehub.exception.BusinessRuleException;
import com.confitec.insurancehub.exception.ConflictException;
import com.confitec.insurancehub.exception.ResourceNotFoundException;
import com.confitec.insurancehub.model.Apolice;
import com.confitec.insurancehub.model.Cliente;
import com.confitec.insurancehub.repository.ApoliceRepository;
import com.confitec.insurancehub.repository.ClienteRepository;
import com.confitec.insurancehub.repository.SinistroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class ApoliceService {

    private final ApoliceRepository apolices;
    private final ClienteRepository clientes;
    private final SinistroRepository sinistros;

    public ApoliceService(ApoliceRepository apolices, ClienteRepository clientes, SinistroRepository sinistros) {
        this.apolices = apolices;
        this.clientes = clientes;
        this.sinistros = sinistros;
    }

    @Transactional(readOnly = true)
    public List<ApoliceResponse> listar(Long clienteId) {
        List<Apolice> lista = (clienteId == null)
                ? apolices.findAllByOrderByIdDesc()
                : apolices.findByClienteIdOrderByIdDesc(clienteId);
        return lista.stream().map(ApoliceResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ApoliceResponse buscar(Long id) {
        return ApoliceResponse.de(obter(id));
    }

    @Transactional
    public ApoliceResponse criar(ApoliceRequest r) {
        String numero = normalizarNumero(r.numero());
        validarRegras(r);
        if (apolices.existsByNumero(numero)) {
            throw new ConflictException("Já existe uma apólice com este número");
        }
        Apolice a = preencher(new Apolice(), r, numero);
        return ApoliceResponse.de(apolices.save(a));
    }

    @Transactional
    public ApoliceResponse atualizar(Long id, ApoliceRequest r) {
        Apolice a = obter(id);
        String numero = normalizarNumero(r.numero());
        validarRegras(r);
        if (apolices.existsByNumeroAndIdNot(numero, id)) {
            throw new ConflictException("Já existe outra apólice com este número");
        }
        return ApoliceResponse.de(apolices.save(preencher(a, r, numero)));
    }

    @Transactional
    public void excluir(Long id) {
        Apolice a = obter(id);
        if (sinistros.existsByApoliceId(id)) {
            throw new ConflictException("Não é possível excluir: a apólice possui sinistros");
        }
        apolices.delete(a);
    }

    private Apolice obter(Long id) {
        return apolices.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Apólice não encontrada"));
    }

    private void validarRegras(ApoliceRequest r) {
        if (!r.dataFim().isAfter(r.dataInicio())) {
            throw new BusinessRuleException("A data de fim deve ser posterior à data de início");
        }
    }

    private Apolice preencher(Apolice a, ApoliceRequest r, String numero) {
        Cliente cliente = clientes.findById(r.clienteId())
                .orElseThrow(() -> new BusinessRuleException("O cliente informado não existe"));
        a.setNumero(numero);
        a.setTipoSeguro(r.tipoSeguro().trim());
        a.setValorPremio(r.valorPremio());
        a.setValorCobertura(r.valorCobertura());
        a.setDataInicio(r.dataInicio());
        a.setDataFim(r.dataFim());
        a.setStatus(r.status());
        a.setCliente(cliente);
        return a;
    }

    private String normalizarNumero(String numero) {
        return numero.trim().toUpperCase(Locale.ROOT);
    }
}
