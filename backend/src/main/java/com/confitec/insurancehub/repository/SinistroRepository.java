package com.confitec.insurancehub.repository;

import com.confitec.insurancehub.model.Sinistro;
import com.confitec.insurancehub.model.StatusSinistro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface SinistroRepository extends JpaRepository<Sinistro, Long> {

    @EntityGraph(attributePaths = {"apolice", "apolice.cliente"})
    List<Sinistro> findAllByOrderByIdDesc();

    @EntityGraph(attributePaths = {"apolice", "apolice.cliente"})
    List<Sinistro> findByApoliceIdOrderByIdDesc(Long apoliceId);

    boolean existsByApoliceId(Long apoliceId);
    long countByStatusIn(Collection<StatusSinistro> status);

    @Query("select sum(s.valorSolicitado) from Sinistro s where s.status in :status")
    BigDecimal somarValores(@Param("status") Collection<StatusSinistro> status);
}
