package com.qualitronix.demo.controller;

import com.qualitronix.demo.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping("/produto/cadastrar")
    public String cadastrarProduto(@RequestParam String nome) {
        return produtoService.cadastrarProduto(nome);
    }
}