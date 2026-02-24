package com.qualitronix.demo.repository;

import com.qualitronix.demo.model.Apontamento;
import com.qualitronix.demo.model.OrdemProducao;
import com.qualitronix.demo.model.StatusApontamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApontamentoRepository extends JpaRepository<Apontamento, Long> {
    Optional<Apontamento> findTopByOrdemProducaoOrderByDataHoraDesc(OrdemProducao op);
    Optional<Apontamento> findByOrdemProducaoAndStatus(
            OrdemProducao op,
            StatusApontamento status
    );
}