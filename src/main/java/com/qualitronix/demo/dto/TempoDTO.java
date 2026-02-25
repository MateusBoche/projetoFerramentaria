package com.qualitronix.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempoDTO {
    private Long totalSegundos;   // valor bruto vindo do banco
    private String horasMinutos;  // valor legível

    // método para calcular horas:minutos
    public void calcularHorasMinutos() {
        long horas = totalSegundos / 3600;
        long minutos = (totalSegundos % 3600) / 60;
        this.horasMinutos = String.format("%d:%02d", horas, minutos);
    }
}