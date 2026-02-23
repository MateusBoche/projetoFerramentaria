package com.qualitronix.demo.service;

import com.qualitronix.demo.model.Maquina;
import com.qualitronix.demo.model.OrdemProducao;
import com.qualitronix.demo.model.Produto;
import com.qualitronix.demo.repository.MaquinaRepository;
import com.qualitronix.demo.repository.OrdemProducaoRepository;
import com.qualitronix.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrdemProducaoService {

    private final OrdemProducaoRepository opRepository;
    private final ProdutoRepository produtoRepository;
    private final MaquinaRepository maquinaRepository;

    public OrdemProducaoService(OrdemProducaoRepository opRepository,
                                ProdutoRepository produtoRepository,
                                MaquinaRepository maquinaRepository) {
        this.opRepository = opRepository;
        this.produtoRepository = produtoRepository;
        this.maquinaRepository = maquinaRepository;
    }

    public String criarOrdemProducao(Long produtoId, Long maquinaId, int quantidade) {
        Optional<Produto> produtoOpt = produtoRepository.findById(produtoId);
        if (produtoOpt.isEmpty()) return "Produto não encontrado";

        Optional<Maquina> maquinaOpt = maquinaRepository.findById(maquinaId);
        if (maquinaOpt.isEmpty()) return "Máquina não encontrada";

        OrdemProducao op = new OrdemProducao();
        op.setProduto(produtoOpt.get());
        op.setMaquina(maquinaOpt.get());
        op.setQuantidade(quantidade);

        // gera código numérico único automático (timestamp + 3 números aleatórios)
        String codigoBarra = gerarCodigoAutomatico();
        op.setQrCode(codigoBarra);  // mantém o campo qrCode, mas agora é numérico
        op.setDataCriacao(LocalDateTime.now());

        opRepository.save(op);

        return "Ordem de Produção criada com sucesso! Código: " + codigoBarra;
    }

    private String gerarCodigoAutomatico() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 900) + 100; // 100 a 999
        return String.valueOf(timestamp) + random;
    }
}