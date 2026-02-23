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

    private String qrCode; // QR da OP

    @ManyToOne
    private Produto produto;

    @ManyToOne
    private Maquina maquina;

    private int quantidade;

    private LocalDateTime dataCriacao; // Data de criação da OP
}