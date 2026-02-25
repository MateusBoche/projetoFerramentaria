package com.qualitronix.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TempoPorOperadorDTO {
    private Long operadorId;
    private String operadorNome;
    private Long totalSegundos;
    private String horasMinutos;

    // Construtor usado pelo JPA/Query para popular os campos do banco
    public TempoPorOperadorDTO(Long operadorId, String operadorNome, Long totalSegundos) {
        this.operadorId = operadorId;
        this.operadorNome = operadorNome;
        this.totalSegundos = totalSegundos;
        this.horasMinutos = null; // será calculado depois no service
    }
}