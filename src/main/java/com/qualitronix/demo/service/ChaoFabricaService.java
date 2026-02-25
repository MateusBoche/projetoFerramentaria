package com.qualitronix.demo.service;

import com.qualitronix.demo.model.*;
import com.qualitronix.demo.repository.ApontamentoRepository;
import com.qualitronix.demo.repository.OrdemProducaoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChaoFabricaService {

    private final OrdemProducaoRepository opRepository;
    private final ApontamentoRepository apontamentoRepository;
    private final OperadorService operadorService;

    public ChaoFabricaService(OrdemProducaoRepository opRepository,
                              ApontamentoRepository apontamentoRepository,
                              OperadorService operadorService) {
        this.opRepository = opRepository;
        this.apontamentoRepository = apontamentoRepository;
        this.operadorService = operadorService;
    }

    // =====================================================
    // 🔁 QR DE EXECUÇÃO (START / PAUSE / RESUME)
    // =====================================================
    public String scanExecucao(String qrCodeExecucao, HttpSession session) {

        Operador operadorSessao =
                operadorService.getOperadorLogado(session);

        OrdemProducao op = opRepository.findByQrCode(qrCodeExecucao)
                .orElseThrow(() -> new RuntimeException("OP não encontrada"));

        if (op.getStatus() == StatusOrdemProducao.FINALIZADA) {
            return "OP já está finalizada";
        }

        // =====================================================
        // ▶️ START — OP ABERTA
        // =====================================================
        if (op.getStatus() == StatusOrdemProducao.ABERTA) {

            if (operadorSessao == null) {
                return "Bipe o QR do operador antes de iniciar a OP";
            }

            Apontamento novo = new Apontamento();
            novo.setOperador(operadorSessao);
            novo.setOrdemProducao(op);
            novo.setDataHora(Instant.now());
            novo.setStatus(StatusApontamento.INICIADO);
            apontamentoRepository.save(novo);

            op.setStatus(StatusOrdemProducao.INICIADA);
            op.setOperadorAtual(operadorSessao);
            opRepository.save(op);

            // 🔴 LOGOUT REAL
            operadorService.logoutOperador(session);

            return "OP iniciada com sucesso";
        }

        // =====================================================
        // ⏯️ PAUSE / RESUME — OP INICIADA
        // =====================================================
        if (op.getStatus() == StatusOrdemProducao.INICIADA) {

            if (operadorSessao == null) {
                return "Bipe o QR do operador para pausar ou retomar a OP";
            }

            if (!operadorSessao.getId().equals(op.getOperadorAtual().getId())) {
                return "Somente o operador atual pode pausar ou retomar a OP";
            }

            Optional<Apontamento> apontamentoAtivo =
                    apontamentoRepository
                            .findTopByOrdemProducaoAndOperadorAndStatusOrderByDataHoraDesc(
                                    op,
                                    operadorSessao,
                                    StatusApontamento.INICIADO
                            );

            // ⏸️ PAUSE
            if (apontamentoAtivo.isPresent()) {

                Apontamento a = apontamentoAtivo.get();
                a.setFim(Instant.now());
                a.setDuracaoSegundos(
                        a.getFim().getEpochSecond()
                                - a.getDataHora().getEpochSecond()
                );
                a.setStatus(StatusApontamento.PAUSADO);
                apontamentoRepository.save(a);

                // 🔴 LOGOUT REAL
                operadorService.logoutOperador(session);

                return "Apontamento pausado";
            }

            // ▶️ RESUME
            Apontamento novo = new Apontamento();
            novo.setOperador(operadorSessao);
            novo.setOrdemProducao(op);
            novo.setDataHora(Instant.now());
            novo.setStatus(StatusApontamento.INICIADO);
            apontamentoRepository.save(novo);

            // 🔴 LOGOUT REAL
            operadorService.logoutOperador(session);

            return "Apontamento retomado";
        }

        return "Ação não permitida";
    }

    // =====================================================
    // 🔒 QR DE FECHAMENTO
    // =====================================================
    public String scanFechamento(String qrCodeFechamento, HttpSession session) {

        Operador operadorSessao =
                operadorService.getOperadorLogado(session);

        if (operadorSessao == null) {
            return "Bipe o QR do operador para finalizar a OP";
        }

        OrdemProducao op = opRepository.findByQrCodeFechamento(qrCodeFechamento)
                .orElseThrow(() -> new RuntimeException("QR inválido"));

        if (op.getStatus() == StatusOrdemProducao.FINALIZADA) {
            return "OP já está finalizada";
        }

        if (!operadorSessao.getId().equals(op.getOperadorAtual().getId())) {
            return "Somente o operador atual pode finalizar a OP";
        }

        apontamentoRepository
                .findTopByOrdemProducaoAndOperadorAndStatusOrderByDataHoraDesc(
                        op,
                        operadorSessao,
                        StatusApontamento.INICIADO
                )
                .ifPresent(a -> {
                    a.setFim(Instant.now());
                    a.setDuracaoSegundos(
                            a.getFim().getEpochSecond()
                                    - a.getDataHora().getEpochSecond()
                    );
                    a.setStatus(StatusApontamento.FINALIZADO);
                    apontamentoRepository.save(a);
                });

        op.setStatus(StatusOrdemProducao.FINALIZADA);
        op.setDataFechamento(LocalDateTime.now());
        op.setOperadorAtual(null);
        opRepository.save(op);

        // 🔴 LOGOUT REAL
        operadorService.logoutOperador(session);

        return "OP finalizada com sucesso";
    }
}