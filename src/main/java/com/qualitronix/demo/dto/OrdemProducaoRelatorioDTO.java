package com.qualitronix.demo.dto;

public class OrdemProducaoRelatorioDTO {
    private Long opId;
    private String qrCode;
    private String produto;
    private String maquina;
    private String operador; // ou lista se quiser todos que trabalharam
    private Long tempoSegundos; // tempo total em segundos
    private String tempoHHMM;   // tempo formatado em horas e minutos
}