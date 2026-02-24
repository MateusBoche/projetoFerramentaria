package com.qualitronix.demo.model;
import com.qualitronix.demo.model.Operador;
import com.qualitronix.demo.model.OrdemProducao;
import com.qualitronix.demo.model.StatusApontamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // ⏱️ Período
    private Instant dataHora;
    private Instant fim;

    // ⏲️ Duração desse período (em segundos)
    private Long duracaoSegundos;

    @Enumerated(EnumType.STRING)
    private StatusApontamento status;
}