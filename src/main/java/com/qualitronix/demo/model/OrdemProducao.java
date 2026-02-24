package com.qualitronix.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdemProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // QR usado para START / PAUSE
    @Column(nullable = false, unique = true)
    private String qrCode;

    // QR exclusivo para FECHAMENTO
    @Column(nullable = false, unique = true)
    private String qrCodeFechamento;

    @ManyToOne
    private Produto produto;

    @ManyToOne
    private Maquina maquina;

    private int quantidade;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataFechamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrdemProducao status = StatusOrdemProducao.ABERTA;

    // Controle de execução por operador
    @ManyToOne
    private Operador operadorAtual; // operador que iniciou o apontamento
}