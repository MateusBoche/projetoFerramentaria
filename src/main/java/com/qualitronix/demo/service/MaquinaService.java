package com.qualitronix.demo.service;

import com.qualitronix.demo.model.Maquina;
import com.qualitronix.demo.repository.MaquinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaquinaService {

    private final MaquinaRepository maquinaRepository;

    public MaquinaService(MaquinaRepository maquinaRepository) {
        this.maquinaRepository = maquinaRepository;
    }

    /* ================= CREATE ================= */
    public Maquina cadastrarMaquina(String nome) {
        String codigoQr = gerarCodigoAutomatico();

        Optional<Maquina> existe = maquinaRepository.findByCodigoQr(codigoQr);
        if (existe.isPresent())
            throw new RuntimeException("Erro: código gerado já existe! Tente novamente.");

        Maquina m = new Maquina();
        m.setNome(nome);
        m.setCodigoQr(codigoQr);
        return maquinaRepository.save(m); // ✅ retorna a máquina criada
    }

    /* ================= READ ================= */

    // Listar todas
    public List<Maquina> listarMaquinas() {
        return maquinaRepository.findAll();
    }

    // Buscar por ID
    public Optional<Maquina> buscarPorId(Long id) {
        return maquinaRepository.findById(id);
    }

    /* ================= UPDATE ================= */
    public Maquina editarMaquina(Long id, String novoNome) {
        Maquina maquina = maquinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Máquina não encontrada."));

        maquina.setNome(novoNome);
        return maquinaRepository.save(maquina); // ✅ retorna a máquina atualizada
    }

    /* ================= DELETE ================= */
    public void excluirMaquina(Long id) {
        if (!maquinaRepository.existsById(id))
            throw new RuntimeException("Máquina não encontrada.");

        maquinaRepository.deleteById(id);
    }

    /* ================= UTIL ================= */
    private String gerarCodigoAutomatico() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 900) + 100;
        return String.valueOf(timestamp) + random;
    }
}