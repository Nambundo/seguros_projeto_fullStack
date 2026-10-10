package com.confitec.insurancehub.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sinistros")
public class Sinistro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false)
    private LocalDate dataOcorrencia;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorSolicitado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSinistro status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "apolice_id", nullable = false)
    private Apolice apolice;

    public Sinistro() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDate getDataOcorrencia() { return dataOcorrencia; }
    public void setDataOcorrencia(LocalDate dataOcorrencia) { this.dataOcorrencia = dataOcorrencia; }
    public BigDecimal getValorSolicitado() { return valorSolicitado; }
    public void setValorSolicitado(BigDecimal valorSolicitado) { this.valorSolicitado = valorSolicitado; }
    public StatusSinistro getStatus() { return status; }
    public void setStatus(StatusSinistro status) { this.status = status; }
    public Apolice getApolice() { return apolice; }
    public void setApolice(Apolice apolice) { this.apolice = apolice; }
}
