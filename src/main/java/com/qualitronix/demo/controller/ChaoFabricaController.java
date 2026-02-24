package com.qualitronix.demo.controller;

import com.qualitronix.demo.service.ChaoFabricaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qrcode")
public class ChaoFabricaController {

    private final ChaoFabricaService chaoFabricaService;

    public ChaoFabricaController(ChaoFabricaService chaoFabricaService) {
        this.chaoFabricaService = chaoFabricaService;
    }

    // -----------------------------
    // 1️⃣ Bipe da OP (start/pause)
    // -----------------------------
    @PostMapping("/op")
    public String scanOp(@RequestParam String qrOp, HttpSession session) {
        return chaoFabricaService.scanExecucao(qrOp, session);
    }

    // -----------------------------
    // 2️⃣ Fechamento da OP
    // -----------------------------
    @PostMapping("/op/finalizar")
    public String finalizarOp(@RequestParam String qrOpFechamento, HttpSession session) {
        return chaoFabricaService.scanFechamento(qrOpFechamento, session);
    }

    // -----------------------------
}