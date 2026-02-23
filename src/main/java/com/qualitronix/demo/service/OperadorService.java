package com.qualitronix.demo.service;

import com.qualitronix.demo.model.Operador;
import com.qualitronix.demo.repository.OperadorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OperadorService {

    private final OperadorRepository operadorRepository;

    // Duração da sessão em minutos
    private static final int DURACAO_SESSAO_MIN = 5;

    public OperadorService(OperadorRepository operadorRepository) {
        this.operadorRepository = operadorRepository;
    }

    /**
     * Login do operador via código de barras
     */
    public String loginOperador(String codigoBarra) {
        Optional<Operador> operadorOpt = operadorRepository.findByMatricula(codigoBarra);
        if (operadorOpt.isEmpty()) {
            return "Operador não encontrado! Código inválido.";
        }

        Operador operador = operadorOpt.get();

        // Verifica se já existe outro operador logado com sessão ativa
        Optional<Operador> operadorLogado = operadorRepository.findAll()
                .stream()
                .filter(Operador::isLogado)
                .filter(o -> o.getSessaoExpiraEm() != null && o.getSessaoExpiraEm().isAfter(LocalDateTime.now()))
                .findFirst();

        if (operadorLogado.isPresent() && !operadorLogado.get().getMatricula().equals(codigoBarra)) {
            return "Outro operador já está logado! Aguarde a sessão expirar.";
        }

        // Marca operador como logado e define expiração da sessão
        operador.setLogado(true);
        operador.setSessaoExpiraEm(LocalDateTime.now().plusMinutes(DURACAO_SESSAO_MIN));
        operadorRepository.save(operador);

        return "Login realizado com sucesso! Operador: " + operador.getNome()
                + " (sessão válida até: " + operador.getSessaoExpiraEm() + ")";
    }

    /**
     * Logout do operador
     */
    public String logoutOperador(String codigoBarra) {
        Optional<Operador> operadorOpt = operadorRepository.findByMatricula(codigoBarra);
        if (operadorOpt.isEmpty()) {
            return "Operador não encontrado!";
        }

        Operador operador = operadorOpt.get();

        if (!operador.isLogado()) {
            return "Operador não está logado!";
        }

        operador.setLogado(false);
        operador.setSessaoExpiraEm(null);
        operadorRepository.save(operador);

        return "Logout realizado com sucesso! Operador: " + operador.getNome();
    }
}