package com.proyectocbr.demo.repository;

import com.proyectocbr.demo.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
}
