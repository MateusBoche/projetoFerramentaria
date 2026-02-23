package com.qualitronix.demo.controller;

import com.qualitronix.demo.service.OperadorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operador")
public class OperadorController {

    private final OperadorService operadorService;

    public OperadorController(OperadorService operadorService) {
        this.operadorService = operadorService;
    }

    // Login do operador via código de barras
    @PostMapping("/login")
    public String login(@RequestParam String codigoBarra) {
        return operadorService.loginOperador(codigoBarra);
    }

    // Logout do operador
    @PostMapping("/logout")
    public String logout(@RequestParam String codigoBarra) {
        return operadorService.logoutOperador(codigoBarra);
    }
}