package com.qualitronix.demo.service;

import com.qualitronix.demo.model.Operador;
import com.qualitronix.demo.repository.OperadorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

@Service
public class OperadorService {

    private final OperadorRepository operadorRepository;
    private static final int DURACAO_SESSAO_MIN = 5;

    public OperadorService(OperadorRepository operadorRepository) {
        this.operadorRepository = operadorRepository;
    }

    /**
     * Scan do operador (primeiro bip LOGA, segundo bip DESLOGA)
     * e guarda na sessão para uso nas OPs
     */
    public String scanOperador(String codigoBarra, HttpSession session) {
        Optional<Operador> operadorOpt = operadorRepository.findByMatricula(codigoBarra);
        if (operadorOpt.isEmpty()) {
            return "Operador não encontrado! Código inválido.";
        }

        Operador operador = operadorOpt.get();
        LocalDateTime agora = LocalDateTime.now();

        // Verifica se já existe algum operador logado ativo
        Optional<Operador> operadorAtivo = operadorRepository.findAll().stream()
                .filter(o -> Boolean.TRUE.equals(o.isLogado()))
                .filter(o -> o.getSessaoExpiraEm() != null && o.getSessaoExpiraEm().isAfter(agora))
                .findFirst();

        // ✅ Se o mesmo operador já está logado → desloga
        if (operadorAtivo.isPresent() && operadorAtivo.get().getId().equals(operador.getId())) {
            operador.setLogado(false);
            operador.setSessaoExpiraEm(null);
            operadorRepository.save(operador);

            session.removeAttribute("OPERADOR_LOGADO");
            return "Operador " + operador.getNome() + " DESLOGADO";
        }

        // ✅ Se outro operador está logado → bloqueia
        if (operadorAtivo.isPresent() && !operadorAtivo.get().getId().equals(operador.getId())) {
            return "Outro operador já está logado! Aguarde a sessão liberar.";
        }

        // 🔹 Nenhum operador ativo → loga o operador
        operador.setLogado(true);
        operador.setSessaoExpiraEm(agora.plusMinutes(DURACAO_SESSAO_MIN));
        operadorRepository.save(operador);

        // Guarda na sessão
        session.setAttribute("OPERADOR_LOGADO", operador);

        return "Operador " + operador.getNome() + " LOGADO (sessão 5 minutos)";
    }

    /**
     * Retorna operador logado da sessão
     */
    public Operador getOperadorLogado(HttpSession session) {
        return (Operador) session.getAttribute("OPERADOR_LOGADO");
    }
}