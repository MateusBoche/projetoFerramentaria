package com.qualitronix.demo.service;

import com.qualitronix.demo.model.Maquina;
import com.qualitronix.demo.repository.MaquinaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MaquinaService {

    private final MaquinaRepository maquinaRepository;

    public MaquinaService(MaquinaRepository maquinaRepository) {
        this.maquinaRepository = maquinaRepository;
    }

    public String cadastrarMaquina(String nome) {
        // Gera um código numérico único automático
        String codigoQr = gerarCodigoAutomatico();

        // Verifica se já existe (por precaução)
        Optional<Maquina> existe = maquinaRepository.findByCodigoQr(codigoQr);
        if (existe.isPresent()) return "Erro: código gerado já existe! Tente novamente.";

        Maquina m = new Maquina();
        m.setNome(nome);
        m.setCodigoQr(codigoQr);
        maquinaRepository.save(m);

        return "Máquina cadastrada com sucesso! Código: " + codigoQr;
    }

    private String gerarCodigoAutomatico() {
        // Usa timestamp + 3 números aleatórios para garantir unicidade
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 900) + 100; // 100 a 999
        return String.valueOf(timestamp) + random;
    }
}