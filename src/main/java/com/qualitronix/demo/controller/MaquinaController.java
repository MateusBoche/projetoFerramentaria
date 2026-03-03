package com.qualitronix.demo.controller;

import com.qualitronix.demo.model.Maquina;
import com.qualitronix.demo.service.MaquinaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/maquina")
@CrossOrigin(origins = "*") // importante para Angular
public class MaquinaController {

    private final MaquinaService maquinaService;

    public MaquinaController(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    /* ================= CREATE ================= */
    @PostMapping("/cadastrar")
    public Maquina cadastrar(@RequestParam String nome) {
        return maquinaService.cadastrarMaquina(nome); // ✅ retorna a máquina criada
    }

    /* ================= READ ================= */
    @GetMapping("/listar")
    public List<Maquina> listar() {
        return maquinaService.listarMaquinas();
    }

    @GetMapping("/{id}")
    public Optional<Maquina> buscarPorId(@PathVariable Long id) {
        return maquinaService.buscarPorId(id);
    }

    /* ================= UPDATE ================= */
    @PutMapping("/editar/{id}")
    public Maquina editar(@PathVariable Long id, @RequestParam String nome) {
        return maquinaService.editarMaquina(id, nome); // ✅ retorna a máquina atualizada
    }

    /* ================= DELETE ================= */
    @DeleteMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        maquinaService.excluirMaquina(id);
        return "Máquina excluída com sucesso!";
    }
}