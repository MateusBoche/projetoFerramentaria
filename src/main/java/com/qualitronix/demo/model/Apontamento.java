package com.qualitronix.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apontamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Operador operador;

    @ManyToOne
    private OrdemProducao ordemProducao;

    private Instant dataHora;

    @Enumerated(EnumType.STRING)
    private StatusApontamento status;
}