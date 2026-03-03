package com.qualitronix.demo.service;

import com.qualitronix.demo.model.Produto;
import com.qualitronix.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /* ================= CREATE ================= */
    public String cadastrarProduto(String nome) {
        String codigo = gerarCodigoAutomatico();

        Optional<Produto> existe = produtoRepository.findByCodigo(codigo);
        if (existe.isPresent()) return "Erro: código gerado já existe! Tente novamente.";

        Produto p = new Produto();
        p.setNome(nome);
        p.setCodigo(codigo);
        produtoRepository.save(p);

        return "Produto cadastrado com sucesso! Código: " + codigo;
    }

    /* ================= READ ================= */
    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    /* ================= UPDATE ================= */
    public String editarProduto(Long id, String novoNome) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isEmpty()) return "Produto não encontrado.";

        Produto produto = produtoOpt.get();
        produto.setNome(novoNome);
        produtoRepository.save(produto);

        return "Produto atualizado com sucesso!";
    }

    /* ================= DELETE ================= */
    public String excluirProduto(Long id) {
        if (!produtoRepository.existsById(id)) return "Produto não encontrado.";

        produtoRepository.deleteById(id);
        return "Produto excluído com sucesso!";
    }

    /* ================= UTIL ================= */
    private String gerarCodigoAutomatico() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 900) + 100; // 100 a 999
        return String.valueOf(timestamp) + random;
    }
}