package com.qualitronix.demo.controller;

import com.qualitronix.demo.service.ChaoFabricaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qrcode")
public class ChaoFabricaController {

    private final ChaoFabricaService chaoFabricaService;

    public ChaoFabricaController(ChaoFabricaService chaoFabricaService) {
        this.chaoFabricaService = chaoFabricaService;
    }

    // -----------------------------
    // 1️⃣ Bipe operador
    // -----------------------------
    @PostMapping("/operador")
    public String scanOperador(@RequestParam String qr) {
        return chaoFabricaService.scanOperador(qr);
    }

    // -----------------------------
    // 2️⃣ Bipe OP (start/pause)
    // -----------------------------
    @PostMapping("/op")
    public String scanOp(@RequestParam String qrOp, @RequestParam String matricula) {
        return chaoFabricaService.scanOrdemProducao(qrOp, matricula);
    }

    // -----------------------------
    // 3️⃣ Finalizar OP
    // -----------------------------
    @PostMapping("/op/finalizar")
    public String finalizarOp(@RequestParam String qrOp, @RequestParam String matricula) {
        return chaoFabricaService.finalizarOrdemProducao(qrOp, matricula);
    }

    // -----------------------------
// 0️⃣ Cadastro de operador
// -----------------------------
    @PostMapping("/operador/cadastrar")
    public String cadastrarOperador(@RequestParam String nome) {
        return chaoFabricaService.cadastrarOperador(nome);
    }
}