package com.qualitronix.demo.controller;

import com.qualitronix.demo.service.OrdemProducaoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/op")
public class OrdemProducaoController {

    private final OrdemProducaoService opService;

    public OrdemProducaoController(OrdemProducaoService opService) {
        this.opService = opService;
    }

    @PostMapping("/criar")
    public String criar(@RequestParam Long produtoId,
                        @RequestParam Long maquinaId,
                        @RequestParam int quantidade) {
        return opService.criarOrdemProducao(produtoId, maquinaId, quantidade);
    }
}