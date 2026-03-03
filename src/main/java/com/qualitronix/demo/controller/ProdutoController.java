package com.qualitronix.demo.controller;

import com.qualitronix.demo.model.Produto;
import com.qualitronix.demo.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produto")
@CrossOrigin(origins = "*") // permite requisições do Angular
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /* ================= CREATE ================= */
    @PostMapping("/cadastrar")
    public String cadastrarProduto(@RequestParam String nome) {
        return produtoService.cadastrarProduto(nome);
    }

    /* ================= READ ================= */
    @GetMapping("/listar")
    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @GetMapping("/{id}")
    public Optional<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    /* ================= UPDATE ================= */
    @PutMapping("/editar/{id}")
    public String editarProduto(@PathVariable Long id, @RequestParam String nome) {
        return produtoService.editarProduto(id, nome);
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable Long id) {
        return produtoService.excluirProduto(id);
    }
}