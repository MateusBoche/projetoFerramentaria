package com.qualitronix.demo.controller;

import com.qualitronix.demo.service.MaquinaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maquina")
public class MaquinaController {

    private final MaquinaService maquinaService;

    public MaquinaController(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@RequestParam String nome) {
        return maquinaService.cadastrarMaquina(nome);
    }
}