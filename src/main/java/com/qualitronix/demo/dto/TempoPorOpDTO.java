package com.qualitronix.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempoPorOpDTO {
    private Long opId;
    private String qrCode;
    private String produtoNome;
    private String maquinaNome;
    private String operadorNome;
    private Long duracaoSegundos;
    private String horasMinutos;

    // Construtor usado pelo JPQL
    public TempoPorOpDTO(Long opId, String qrCode, String produtoNome, String maquinaNome, String operadorNome, Long duracaoSegundos) {
        this.opId = opId;
        this.qrCode = qrCode;
        this.produtoNome = produtoNome;
        this.maquinaNome = maquinaNome;
        this.operadorNome = operadorNome;
        this.duracaoSegundos = duracaoSegundos;
        this.horasMinutos = null; // será calculado depois no service
    }
}