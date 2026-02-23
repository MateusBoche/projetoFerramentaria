package com.qualitronix.demo.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Operador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String matricula; // ou código QR
    @Column(nullable = false)
    private boolean logado = false;
    private LocalDateTime sessaoExpiraEm;
}