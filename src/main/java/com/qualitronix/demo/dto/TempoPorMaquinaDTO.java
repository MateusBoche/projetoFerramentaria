package com.qualitronix.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TempoPorMaquinaDTO {
    private Long maquinaId;
    private String maquinaNome;
    private Long totalSegundos;
    private String horasMinutos;
    public TempoPorMaquinaDTO(Long maquinaId, String maquinaNome, Long totalSegundos) {
        this.maquinaId = maquinaId;
        this.maquinaNome = maquinaNome;
        this.totalSegundos = totalSegundos;
        this.horasMinutos = null; // será calculado depois
    }
}