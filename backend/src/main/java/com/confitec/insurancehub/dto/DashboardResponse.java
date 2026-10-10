package com.confitec.insurancehub.dto;

import java.math.BigDecimal;

public record DashboardResponse(
        long totalClientes,
        long apolicesAtivas,
        long sinistrosEmAndamento,
        BigDecimal premiosAtivos,
        BigDecimal coberturaAtiva,
        BigDecimal valorEmSinistrosEmAndamento) {
}
