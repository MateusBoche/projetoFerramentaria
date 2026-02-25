package com.qualitronix.demo.controller;

import com.qualitronix.demo.dto.TempoPorMaquinaDTO;
import com.qualitronix.demo.dto.TempoPorOpDTO;
import com.qualitronix.demo.dto.TempoPorOperadorDTO;
import com.qualitronix.demo.dto.TempoPorOperadorDetalhadoDTO;
import com.qualitronix.demo.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    // =====================================================
    // Relatório de tempo por OP
    // =====================================================
    @GetMapping("/tempo/op")
    public List<TempoPorOpDTO> tempoPorOp() {
        return relatorioService.getTempoPorOp();
    }

    // =====================================================
    // Relatório de tempo por operador
    // =====================================================
    @GetMapping("/tempo/operador")
    public List<TempoPorOperadorDTO> tempoPorOperador() {
        return relatorioService.getTempoPorOperador();
    }

    // =====================================================
    // Relatório de tempo por máquina
    // =====================================================
    @GetMapping("/tempo/maquina")
    public List<TempoPorMaquinaDTO> tempoPorMaquina() {
        return relatorioService.getTempoPorMaquina();
    }

    // =====================================================
// Relatório de tempo detalhado por operador
// =====================================================
    @GetMapping("/tempo/operador/detalhado")
    public List<TempoPorOperadorDetalhadoDTO> tempoPorOperadorDetalhado() {
        return relatorioService.getTempoPorOperadorDetalhado();
    }
}