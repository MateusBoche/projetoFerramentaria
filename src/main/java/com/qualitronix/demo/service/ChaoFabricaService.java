package com.qualitronix.demo.service;

import com.qualitronix.demo.model.Apontamento;
import com.qualitronix.demo.model.Operador;
import com.qualitronix.demo.model.OrdemProducao;
import com.qualitronix.demo.model.StatusApontamento;
import com.qualitronix.demo.model.StatusOrdemProducao;
import com.qualitronix.demo.repository.ApontamentoRepository;
import com.qualitronix.demo.repository.OperadorRepository;
import com.qualitronix.demo.repository.OrdemProducaoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChaoFabricaService {

    private final OperadorRepository operadorRepository;
    private final OrdemProducaoRepository opRepository;
    private final ApontamentoRepository apontamentoRepository;

    public ChaoFabricaService(OperadorRepository operadorRepository,
                              OrdemProducaoRepository opRepository,
                              ApontamentoRepository apontamentoRepository) {
        this.operadorRepository = operadorRepository;
        this.opRepository = opRepository;
        this.apontamentoRepository = apontamentoRepository;
    }

    // =====================================================
    // 🔁 QR DE EXECUÇÃO (START / PAUSE)
    // =====================================================
    public String scanExecucao(String qrCodeExecucao, HttpSession session) {

        Operador operador = (Operador) session.getAttribute("OPERADOR_LOGADO");
        if (operador == null) {
            throw new RuntimeException("Nenhum operador logado!");
        }

        OrdemProducao op = opRepository.findByQrCode(qrCodeExecucao)
                .orElseThrow(() -> new RuntimeException("OP não encontrada"));

        // OP já finalizada → nada a fazer
        if (op.getStatus() == StatusOrdemProducao.FINALIZADA) {
            return "OP já está finalizada";
        }

        // 🔒 OP iniciada por outro operador → bloqueia
        if (op.getStatus() == StatusOrdemProducao.INICIADA && !operador.equals(op.getOperadorAtual())) {
            return "OP já está em execução por outro operador! Bloqueada.";
        }

        // ⏸️ PAUSE → só operador atual pode pausar
        if (op.getStatus() == StatusOrdemProducao.INICIADA && operador.equals(op.getOperadorAtual())) {
            Optional<Apontamento> apontamentoAbertoOpt =
                    apontamentoRepository.findByOrdemProducaoAndStatus(op, StatusApontamento.INICIADO);

            apontamentoAbertoOpt.ifPresent(a -> {
                a.setFim(Instant.now());
                a.setDuracaoSegundos(a.getFim().getEpochSecond() - a.getDataHora().getEpochSecond());
                a.setStatus(StatusApontamento.FINALIZADO);
                apontamentoRepository.save(a);
            });

            op.setStatus(StatusOrdemProducao.ABERTA);
            op.setOperadorAtual(null); // libera OP para START futuro
            opRepository.save(op);

            return "Apontamento pausado pelo operador " + operador.getNome();
        }

        // ▶️ START → inicia apontamento se OP estiver ABERTA
        if (op.getStatus() == StatusOrdemProducao.ABERTA) {
            Apontamento novo = new Apontamento();
            novo.setOperador(operador);
            novo.setOrdemProducao(op);
            novo.setDataHora(Instant.now());
            novo.setStatus(StatusApontamento.INICIADO);
            apontamentoRepository.save(novo);

            op.setStatus(StatusOrdemProducao.INICIADA);
            op.setOperadorAtual(operador);
            opRepository.save(op);

            return "Apontamento iniciado pelo operador " + operador.getNome();
        }

        return "Erro inesperado ao processar OP!";
    }

    // =====================================================
    // 🔒 QR DE FECHAMENTO DA OP
    // =====================================================
    public String scanFechamento(String qrCodeFechamento, HttpSession session) {

        Operador operador = (Operador) session.getAttribute("OPERADOR_LOGADO");
        if (operador == null) {
            throw new RuntimeException("Nenhum operador logado!");
        }

        OrdemProducao op = opRepository.findByQrCodeFechamento(qrCodeFechamento)
                .orElseThrow(() -> new RuntimeException("QR de fechamento inválido"));

        if (op.getStatus() == StatusOrdemProducao.FINALIZADA) {
            return "OP já está finalizada";
        }

        // 🔒 só operadorAtual pode fechar
        if (!operador.equals(op.getOperadorAtual())) {
            return "Somente o operador que iniciou a OP pode fechar!";
        }

        // finaliza apontamento aberto
        Optional<Apontamento> apontamentoAbertoOpt =
                apontamentoRepository.findByOrdemProducaoAndStatus(op, StatusApontamento.INICIADO);

        apontamentoAbertoOpt.ifPresent(a -> {
            a.setFim(Instant.now());
            a.setDuracaoSegundos(a.getFim().getEpochSecond() - a.getDataHora().getEpochSecond());
            a.setStatus(StatusApontamento.FINALIZADO);
            apontamentoRepository.save(a);
        });

        op.setStatus(StatusOrdemProducao.FINALIZADA);
        op.setDataFechamento(LocalDateTime.now());
        op.setOperadorAtual(null); // libera OP completamente
        opRepository.save(op);

        return "OP finalizada com sucesso pelo operador " + operador.getNome();
    }
}