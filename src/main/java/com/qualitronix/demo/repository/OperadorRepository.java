package com.qualitronix.demo.repository;

import com.qualitronix.demo.model.Operador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Long> {
    Optional<Operador> findByMatricula(String matricula);
    Optional<Operador> findFirstByLogadoTrueAndSessaoExpiraEmAfter(LocalDateTime now);

}