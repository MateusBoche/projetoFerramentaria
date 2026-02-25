package com.qualitronix.demo.service;

import com.qualitronix.demo.model.Operador;
import com.qualitronix.demo.repository.OperadorRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OperadorService {

    private final OperadorRepository operadorRepository;
    private static final int DURACAO_SESSAO_MIN = 5;

    public OperadorService(OperadorRepository operadorRepository) {
        this.operadorRepository = operadorRepository;
    }

    /**
     * Scan do operador
     * - Primeiro bip → LOGA
     * - Segundo bip → DESLOGA
     */
    public String scanOperador(String codigoBarra, HttpSession session) {

        Operador operador = operadorRepository.findByMatricula(codigoBarra)
                .orElseThrow(() -> new RuntimeException("Operador não encontrado"));

        LocalDateTime agora = LocalDateTime.now();

        Optional<Operador> operadorAtivo = operadorRepository.findAll().stream()
                .filter(o -> Boolean.TRUE.equals(o.isLogado()))
                .filter(o -> o.getSessaoExpiraEm() != null && o.getSessaoExpiraEm().isAfter(agora))
                .findFirst();

        // 🔁 MESMO OPERADOR → DESLOGA
        if (operadorAtivo.isPresent()
                && operadorAtivo.get().getId().equals(operador.getId())) {

            operador.setLogado(false);
            operador.setSessaoExpiraEm(null);
            operadorRepository.save(operador);

            session.removeAttribute("OPERADOR_LOGADO");

            return "Operador " + operador.getNome() + " DESLOGADO";
        }

        // 🚫 OUTRO OPERADOR ATIVO
        if (operadorAtivo.isPresent()) {
            return "Outro operador já está logado! Aguarde.";
        }

        // ✅ LOGA
        operador.setLogado(true);
        operador.setSessaoExpiraEm(agora.plusMinutes(DURACAO_SESSAO_MIN));
        operadorRepository.save(operador);

        session.setAttribute("OPERADOR_LOGADO", operador);

        return "Operador " + operador.getNome() + " LOGADO";
    }

    /**
     * 🔑 Logout FORÇADO (usado pela OP)
     */
    public void logoutOperador(HttpSession session) {

        Operador operadorSessao =
                (Operador) session.getAttribute("OPERADOR_LOGADO");

        if (operadorSessao != null) {
            operadorSessao.setLogado(false);
            operadorSessao.setSessaoExpiraEm(null);
            operadorRepository.save(operadorSessao);

            session.removeAttribute("OPERADOR_LOGADO");
        }
    }

    public Operador getOperadorLogado(HttpSession session) {
        return (Operador) session.getAttribute("OPERADOR_LOGADO");
    }
}