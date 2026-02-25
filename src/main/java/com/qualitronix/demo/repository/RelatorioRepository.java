package com.qualitronix.demo.repository;

import com.qualitronix.demo.dto.TempoPorMaquinaDTO;
import com.qualitronix.demo.dto.TempoPorOpDTO;
import com.qualitronix.demo.dto.TempoPorOperadorDTO;
import com.qualitronix.demo.dto.TempoPorOperadorDetalhadoDTO;
import com.qualitronix.demo.model.Apontamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelatorioRepository extends JpaRepository<Apontamento, Long> {

    // Tempo por OP
    @Query("""
        SELECT new com.qualitronix.demo.dto.TempoPorOpDTO(
            op.id,
            op.qrCode,
            p.nome,
            m.nome,
            o.nome,
            SUM(a.duracaoSegundos)
        )
        FROM Apontamento a
        JOIN a.ordemProducao op
        JOIN op.produto p
        JOIN op.maquina m
        JOIN a.operador o
        WHERE a.status IN ('PAUSADO','FINALIZADO')
        GROUP BY op.id, op.qrCode, p.nome, m.nome, o.nome
        ORDER BY SUM(a.duracaoSegundos) DESC
    """)
    List<TempoPorOpDTO> tempoPorOp();

    // Tempo total por operador
    @Query("""
        SELECT new com.qualitronix.demo.dto.TempoPorOperadorDTO(
            o.id,
            o.nome,
            SUM(a.duracaoSegundos)
        )
        FROM Apontamento a
        JOIN a.operador o
        WHERE a.status IN ('PAUSADO','FINALIZADO')
        GROUP BY o.id, o.nome
        ORDER BY SUM(a.duracaoSegundos) DESC
    """)
    List<TempoPorOperadorDTO> tempoPorOperador();

    // Tempo total por máquina
    @Query("""
        SELECT new com.qualitronix.demo.dto.TempoPorMaquinaDTO(
            m.id,
            m.nome,
            SUM(a.duracaoSegundos)
        )
        FROM Apontamento a
        JOIN a.ordemProducao op
        JOIN op.maquina m
        WHERE a.status IN ('PAUSADO','FINALIZADO')
        GROUP BY m.id, m.nome
        ORDER BY SUM(a.duracaoSegundos) DESC
    """)
    List<TempoPorMaquinaDTO> tempoPorMaquina();

    @Query("""
    SELECT new com.qualitronix.demo.dto.TempoPorOpDTO(
        a.id,
        op.qrCode,
        p.nome,
        m.nome,
        o.nome,
        SUM(a.duracaoSegundos)
    )
    FROM Apontamento a
    JOIN a.operador o
    JOIN a.ordemProducao op
    JOIN op.maquina m
    JOIN op.produto p
    GROUP BY a.id, op.qrCode, p.nome, m.nome, o.nome
""")
    List<TempoPorOpDTO> tempoDetalhadoPorOp();


    @Query("SELECT new com.qualitronix.demo.dto.TempoPorOperadorDetalhadoDTO(" +
            "operador.id, operador.nome, maq.id, maq.nome, prod.id, prod.nome, SUM(a.duracaoSegundos)) " +
            "FROM Apontamento a " +
            "JOIN a.operador operador " +
            "JOIN a.ordemProducao op " +
            "JOIN op.maquina maq " +
            "JOIN op.produto prod " +
            "GROUP BY operador.id, operador.nome, maq.id, maq.nome, prod.id, prod.nome")
    List<TempoPorOperadorDetalhadoDTO> tempoPorOperadorDetalhado();
}