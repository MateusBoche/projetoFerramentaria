package com.qualitronix.demo.repository;

import com.qualitronix.demo.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaquinaRepository extends JpaRepository<Maquina, Long> {
    Optional<Maquina> findByCodigoQr(String codigoQr);
}