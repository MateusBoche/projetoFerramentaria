package com.qualitronix.demo.repository;

import com.qualitronix.demo.model.OrdemProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdemProducaoRepository extends JpaRepository<OrdemProducao, Long> {
    Optional<OrdemProducao> findByQrCode(String qrCode);
    Optional<OrdemProducao> findByQrCodeFechamento(String qrCodeFechamento);
}