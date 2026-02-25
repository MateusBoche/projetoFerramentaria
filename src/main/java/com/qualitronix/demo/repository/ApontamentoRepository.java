package com.qualitronix.demo.repository;

import com.qualitronix.demo.model.Apontamento;
import com.qualitronix.demo.model.Operador;
import com.qualitronix.demo.model.OrdemProducao;
import com.qualitronix.demo.model.StatusApontamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApontamentoRepository extends JpaRepository<Apontamento, Long> {
    Optional<Apontamento> findTopByOrdemProducaoAndOperadorAndStatusOrderByDataHoraDesc(
            OrdemProducao ordemProducao,
            Operador operador,
            StatusApontamento status
    );

    // retorna o último apontamento da OP
    Optional<Apontamento> findTopByOrdemProducaoOrderByDataHoraDesc(OrdemProducao op);

    // retorna um apontamento da OP com status específico
    Optional<Apontamento> findByOrdemProducaoAndStatus(OrdemProducao op, StatusApontamento status);

    // retorna todos os apontamentos da OP com status específico
    List<Apontamento> findAllByOrdemProducaoAndStatus(OrdemProducao op, StatusApontamento status);
}