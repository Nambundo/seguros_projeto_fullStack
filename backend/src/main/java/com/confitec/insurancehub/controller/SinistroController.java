package com.confitec.insurancehub.controller;

import com.confitec.insurancehub.dto.SinistroRequest;
import com.confitec.insurancehub.dto.SinistroResponse;
import com.confitec.insurancehub.service.SinistroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sinistros")
public class SinistroController {

    private final SinistroService service;

    public SinistroController(SinistroService service) {
        this.service = service;
    }

    /** Filtro opcional: /api/sinistros?apoliceId=1 */
    @GetMapping
    public List<SinistroResponse> listar(@RequestParam(required = false) Long apoliceId) {
        return service.listar(apoliceId);
    }

    @GetMapping("/{id}")
    public SinistroResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SinistroResponse criar(@Valid @RequestBody SinistroRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    public SinistroResponse atualizar(@PathVariable Long id, @Valid @RequestBody SinistroRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
