package com.confitec.insurancehub.controller;

import com.confitec.insurancehub.dto.ApoliceRequest;
import com.confitec.insurancehub.dto.ApoliceResponse;
import com.confitec.insurancehub.service.ApoliceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apolices")
public class ApoliceController {

    private final ApoliceService service;

    public ApoliceController(ApoliceService service) {
        this.service = service;
    }

    /** Filtro opcional: /api/apolices?clienteId=1 */
    @GetMapping
    public List<ApoliceResponse> listar(@RequestParam(required = false) Long clienteId) {
        return service.listar(clienteId);
    }

    @GetMapping("/{id}")
    public ApoliceResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApoliceResponse criar(@Valid @RequestBody ApoliceRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    public ApoliceResponse atualizar(@PathVariable Long id, @Valid @RequestBody ApoliceRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
