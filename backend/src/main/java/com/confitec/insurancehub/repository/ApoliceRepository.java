package com.confitec.insurancehub.repository;

import com.confitec.insurancehub.model.Apolice;
import com.confitec.insurancehub.model.StatusApolice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ApoliceRepository extends JpaRepository<Apolice, Long> {

    // EntityGraph traz o cliente no mesmo SELECT (evita o problema N+1).
    @EntityGraph(attributePaths = "cliente")
    List<Apolice> findAllByOrderByIdDesc();

    @EntityGraph(attributePaths = "cliente")
    List<Apolice> findByClienteIdOrderByIdDesc(Long clienteId);

    boolean existsByNumero(String numero);
    boolean existsByNumeroAndIdNot(String numero, Long id);
    boolean existsByClienteId(Long clienteId);
    long countByStatus(StatusApolice status);

    @Query("select sum(a.valorPremio) from Apolice a where a.status = :status")
    BigDecimal somarPremios(@Param("status") StatusApolice status);

    @Query("select sum(a.valorCobertura) from Apolice a where a.status = :status")
    BigDecimal somarCoberturas(@Param("status") StatusApolice status);
}
