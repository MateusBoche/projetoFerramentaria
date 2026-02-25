package com.qualitronix.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TempoPorOperadorDetalhadoDTO {
    private Long operadorId;
    private String operadorNome;
    private Long maquinaId;
    private String maquinaNome;
    private Long produtoId;
    private String produtoNome;
    private Long totalSegundos;
    private String horasMinutos; // será calculado depois

    // Construtor para a query do JPA
    public TempoPorOperadorDetalhadoDTO(Long operadorId, String operadorNome,
                                        Long maquinaId, String maquinaNome,
                                        Long produtoId, String produtoNome,
                                        Long totalSegundos) {
        this.operadorId = operadorId;
        this.operadorNome = operadorNome;
        this.maquinaId = maquinaId;
        this.maquinaNome = maquinaNome;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.totalSegundos = totalSegundos;
        this.horasMinutos = null; // será calculado depois no service
    }
}