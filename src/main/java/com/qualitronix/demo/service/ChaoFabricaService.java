package com.qualitronix.demo.service;

import com.qualitronix.demo.model.*;
import com.qualitronix.demo.repository.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ChaoFabricaService {

    private final OperadorRepository operadorRepository;
    private final OrdemProducaoRepository opRepository;
    private final ApontamentoRepository apontamentoRepository;

    // Sessões ativas: operadorId -> timestamp da última bipagem
    private final Map<Long, Instant> operadorSessao = new HashMap<>();

    public ChaoFabricaService(OperadorRepository operadorRepository,
                              OrdemProducaoRepository opRepository,
                              ApontamentoRepository apontamentoRepository) {
        this.operadorRepository = operadorRepository;
        this.opRepository = opRepository;
        this.apontamentoRepository = apontamentoRepository;
    }

    // -----------------------------
    // 1️⃣ Scan do operador (inicia sessão 5 min)
    // -----------------------------
    public String scanOperador(String matricula) {
        Optional<Operador> op = operadorRepository.findByMatricula(matricula);
        if (op.isEmpty()) return "Operador não encontrado";

        operadorSessao.put(op.get().getId(), Instant.now());
        return "Sessão iniciada para operador " + op.get().getNome() + " (válida 5 minutos)";
    }

    // -----------------------------
    // 2️⃣ Scan da OP (start/pause/finalizar)
    // -----------------------------
    public String scanOrdemProducao(String qrOp, String matricula) {
        // valida operador
        Optional<Operador> operadorOpt = operadorRepository.findByMatricula(matricula);
        if (operadorOpt.isEmpty()) return "Operador não encontrado";

        Operador operador = operadorOpt.get();

        // valida sessão
        Instant inicioSessao = operadorSessao.get(operador.getId());
        if (inicioSessao == null || Instant.now().isAfter(inicioSessao.plusSeconds(300))) {
            return "Sessão expirada. Bipe novamente o QR do operador.";
        }

        // valida OP
        Optional<OrdemProducao> opOpt = opRepository.findByQrCode(qrOp);
        if (opOpt.isEmpty()) return "OP não encontrada";

        OrdemProducao op = opOpt.get();

        // pega último apontamento
        Apontamento ultimoApontamento = apontamentoRepository
                .findTopByOrdemProducaoOrderByDataHoraDesc(op)
                .orElse(null);

        // lógica start/pause
        if (ultimoApontamento == null || ultimoApontamento.getStatus() == StatusApontamento.PAUSADO) {
            // start
            Apontamento a = new Apontamento(null, operador, op, Instant.now(), StatusApontamento.INICIADO);
            apontamentoRepository.save(a);
            return "OP " + op.getId() + " iniciada";
        } else if (ultimoApontamento.getStatus() == StatusApontamento.INICIADO) {
            // pause
            Apontamento a = new Apontamento(null, operador, op, Instant.now(), StatusApontamento.PAUSADO);
            apontamentoRepository.save(a);
            return "OP " + op.getId() + " pausada";
        }

        return "Ação inválida";
    }

    // -----------------------------
    // 3️⃣ Finalizar OP
    // -----------------------------
    public String finalizarOrdemProducao(String qrOp, String matricula) {
        // valida operador
        Optional<Operador> operadorOpt = operadorRepository.findByMatricula(matricula);
        if (operadorOpt.isEmpty()) return "Operador não encontrado";

        Operador operador = operadorOpt.get();

        // valida sessão
        Instant inicioSessao = operadorSessao.get(operador.getId());
        if (inicioSessao == null || Instant.now().isAfter(inicioSessao.plusSeconds(300))) {
            return "Sessão expirada. Bipe novamente o QR do operador.";
        }

        // valida OP
        Optional<OrdemProducao> opOpt = opRepository.findByQrCode(qrOp);
        if (opOpt.isEmpty()) return "OP não encontrada";

        OrdemProducao op = opOpt.get();

        // cria apontamento de finalização
        Apontamento a = new Apontamento(null, operador, op, Instant.now(), StatusApontamento.FINALIZADO);
        apontamentoRepository.save(a);

        return "OP " + op.getId() + " finalizada";
    }


    public String cadastrarOperador(String nome) {
        // Gera matrícula automática (timestamp + 3 números aleatórios)
        String matricula = gerarMatriculaAutomatica();

        // Verifica se a matrícula já existe (precaução)
        Optional<Operador> opExistente = operadorRepository.findByMatricula(matricula);
        if (opExistente.isPresent()) {
            return "Erro: matrícula gerada já existe! Tente novamente.";
        }

        // Criar e salvar operador
        Operador operador = new Operador();
        operador.setNome(nome);
        operador.setMatricula(matricula);

        operadorRepository.save(operador);

        return "Operador cadastrado com sucesso! Matrícula: " + matricula;
    }

    // Função para gerar matrícula automática
    private String gerarMatriculaAutomatica() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 900) + 100; // 100 a 999
        return String.valueOf(timestamp) + random;
    }
}