package com.qualitronix.demo.controller;

import com.qualitronix.demo.service.OperadorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operador")
public class OperadorController {

    private final OperadorService operadorService;

    public OperadorController(OperadorService operadorService) {
        this.operadorService = operadorService;
    }

    // -----------------------------
    // Scan do operador (primeiro bip LOGA, segundo bip DESLOGA)
    // Guarda operador na sessão para usar nas OPs
    // -----------------------------
    @PostMapping("/scan")
    public String scanOperador(@RequestParam String codigoBarra, HttpSession session) {
        return operadorService.scanOperador(codigoBarra, session);
    }
}