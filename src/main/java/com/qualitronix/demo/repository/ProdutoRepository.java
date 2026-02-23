package com.qualitronix.demo.repository;

import com.qualitronix.demo.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByCodigo(String codigo);
}