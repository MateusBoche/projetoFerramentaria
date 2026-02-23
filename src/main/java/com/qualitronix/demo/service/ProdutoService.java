package com.qualitronix.demo.service;

import com.qualitronix.demo.model.Produto;
import com.qualitronix.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public String cadastrarProduto(String nome) {
        // Gera código numérico automático
        String codigo = gerarCodigoAutomatico();

        // Verifica se já existe (precaução)
        Optional<Produto> existe = produtoRepository.findByCodigo(codigo);
        if (existe.isPresent()) return "Erro: código gerado já existe! Tente novamente.";

        Produto p = new Produto();
        p.setNome(nome);
        p.setCodigo(codigo);
        produtoRepository.save(p);

        return "Produto cadastrado com sucesso! Código: " + codigo;
    }

    // Função para gerar código automático
    private String gerarCodigoAutomatico() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 900) + 100; // 100 a 999
        return String.valueOf(timestamp) + random;
    }
}