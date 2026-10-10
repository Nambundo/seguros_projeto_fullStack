package com.confitec.insurancehub.service;

import com.confitec.insurancehub.dto.SinistroRequest;
import com.confitec.insurancehub.dto.SinistroResponse;
import com.confitec.insurancehub.exception.BusinessRuleException;
import com.confitec.insurancehub.model.Apolice;
import com.confitec.insurancehub.model.Cliente;
import com.confitec.insurancehub.model.StatusApolice;
import com.confitec.insurancehub.model.StatusSinistro;
import com.confitec.insurancehub.repository.ApoliceRepository;
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
class SinistroServiceTest {

    @Mock
    private SinistroRepository sinistros;

    @Mock
    private ApoliceRepository apolices;

    private SinistroService service;
    private Apolice apolice;

    @BeforeEach
    void preparar() {
        service = new SinistroService(sinistros, apolices);

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Maria");

        apolice = new Apolice();
        apolice.setId(10L);
        apolice.setNumero("AP-001");
        apolice.setStatus(StatusApolice.ATIVA);
        apolice.setValorCobertura(new BigDecimal("80000.00"));
        apolice.setDataInicio(LocalDate.now().minusMonths(2));
        apolice.setDataFim(LocalDate.now().plusMonths(10));
        apolice.setCliente(cliente);

        when(apolices.findById(10L)).thenReturn(Optional.of(apolice));
        when(sinistros.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    private SinistroRequest pedido(LocalDate data, String valor) {
        return new SinistroRequest("Colisão", data, new BigDecimal(valor), StatusSinistro.ABERTO, 10L);
    }

    @Test
    void criaSinistroValido() {
        SinistroResponse resp = service.criar(pedido(LocalDate.now().minusDays(1), "5000.00"));
        assertEquals("AP-001", resp.apoliceNumero());
        assertEquals("Maria", resp.clienteNome());
    }

    @Test
    void rejeitaValorAcimaDaCobertura() {
        assertThrows(BusinessRuleException.class,
                () -> service.criar(pedido(LocalDate.now().minusDays(1), "90000.00")));
    }

    @Test
    void rejeitaDataForaDaVigencia() {
        assertThrows(BusinessRuleException.class,
                () -> service.criar(pedido(LocalDate.now().minusYears(1), "1000.00")));
    }

    @Test
    void rejeitaDataNoFuturo() {
        assertThrows(BusinessRuleException.class,
                () -> service.criar(pedido(LocalDate.now().plusDays(3), "1000.00")));
    }

    @Test
    void rejeitaSinistroEmApoliceNaoAtiva() {
        apolice.setStatus(StatusApolice.CANCELADA);
        assertThrows(BusinessRuleException.class,
                () -> service.criar(pedido(LocalDate.now().minusDays(1), "1000.00")));
    }
}
