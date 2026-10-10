package com.confitec.insurancehub.service;

import com.confitec.insurancehub.dto.ApoliceRequest;
import com.confitec.insurancehub.exception.BusinessRuleException;
import com.confitec.insurancehub.exception.ConflictException;
import com.confitec.insurancehub.model.Apolice;
import com.confitec.insurancehub.model.Cliente;
import com.confitec.insurancehub.model.StatusApolice;
import com.confitec.insurancehub.repository.ApoliceRepository;
import com.confitec.insurancehub.repository.ClienteRepository;
import com.confitec.insurancehub.repository.SinistroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ApoliceServiceTest {

    @Mock
    private ApoliceRepository apolices;

    @Mock
    private ClienteRepository clientes;

    @Mock
    private SinistroRepository sinistros;

    private ApoliceService service;

    @BeforeEach
    void preparar() {
        service = new ApoliceService(apolices, clientes, sinistros);

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Maria");
        when(clientes.findById(1L)).thenReturn(Optional.of(cliente));
        when(apolices.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    private ApoliceRequest pedido(String numero, LocalDate inicio, LocalDate fim) {
        return new ApoliceRequest(numero, "Auto", new BigDecimal("1500.00"), new BigDecimal("80000.00"),
                inicio, fim, StatusApolice.ATIVA, 1L);
    }

    @Test
    void normalizaONumeroDaApolice() {
        var resp = service.criar(pedido("  ap-001 ", LocalDate.now(), LocalDate.now().plusYears(1)));
        assertEquals("AP-001", resp.numero());
        assertEquals("Maria", resp.clienteNome());
    }

    @Test
    void rejeitaDataFimAnteriorAoInicio() {
        assertThrows(BusinessRuleException.class,
                () -> service.criar(pedido("AP-002", LocalDate.now(), LocalDate.now().minusDays(1))));
    }

    @Test
    void rejeitaNumeroDuplicado() {
        when(apolices.existsByNumero("AP-003")).thenReturn(true);
        assertThrows(ConflictException.class,
                () -> service.criar(pedido("AP-003", LocalDate.now(), LocalDate.now().plusYears(1))));
    }
}
