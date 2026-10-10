package com.confitec.insurancehub.service;

import com.confitec.insurancehub.dto.ClienteRequest;
import com.confitec.insurancehub.dto.ClienteResponse;
import com.confitec.insurancehub.exception.ConflictException;
import com.confitec.insurancehub.exception.ResourceNotFoundException;
import com.confitec.insurancehub.model.Cliente;
import com.confitec.insurancehub.repository.ApoliceRepository;
import com.confitec.insurancehub.repository.ClienteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class ClienteService {

    private final ClienteRepository clientes;
    private final ApoliceRepository apolices;

    public ClienteService(ClienteRepository clientes, ApoliceRepository apolices) {
        this.clientes = clientes;
        this.apolices = apolices;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clientes.findAll(Sort.by("nome")).stream().map(ClienteResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscar(Long id) {
        return ClienteResponse.de(obter(id));
    }

    @Transactional
    public ClienteResponse criar(ClienteRequest r) {
        String cpf = somenteDigitos(r.cpf());
        String email = normalizarEmail(r.email());
        if (clientes.existsByCpf(cpf)) {
            throw new ConflictException("Já existe um cliente com este CPF");
        }
        if (clientes.existsByEmail(email)) {
            throw new ConflictException("Já existe um cliente com este e-mail");
        }
        return ClienteResponse.de(clientes.save(preencher(new Cliente(), r, cpf, email)));
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest r) {
        Cliente c = obter(id);
        String cpf = somenteDigitos(r.cpf());
        String email = normalizarEmail(r.email());
        if (clientes.existsByCpfAndIdNot(cpf, id)) {
            throw new ConflictException("Já existe outro cliente com este CPF");
        }
        if (clientes.existsByEmailAndIdNot(email, id)) {
            throw new ConflictException("Já existe outro cliente com este e-mail");
        }
        return ClienteResponse.de(clientes.save(preencher(c, r, cpf, email)));
    }

    @Transactional
    public void excluir(Long id) {
        Cliente c = obter(id);
        if (apolices.existsByClienteId(id)) {
            throw new ConflictException("Não é possível excluir: o cliente possui apólices");
        }
        clientes.delete(c);
    }

    private Cliente obter(Long id) {
        return clientes.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    private Cliente preencher(Cliente c, ClienteRequest r, String cpf, String email) {
        c.setNome(r.nome().trim());
        c.setEmail(email);
        c.setCpf(cpf);
        return c;
    }

    private String somenteDigitos(String valor) {
        return valor.replaceAll("\\D", "");
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
