package com.qualitronix.demo.repository;

import com.qualitronix.demo.model.Apontamento;
import com.qualitronix.demo.model.OrdemProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApontamentoRepository extends JpaRepository<Apontamento, Long> {
    Optional<Apontamento> findTopByOrdemProducaoOrderByDataHoraDesc(OrdemProducao op);
}