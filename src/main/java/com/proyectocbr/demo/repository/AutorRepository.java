package com.proyectocbr.demo.repository;

import com.proyectocbr.demo.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}

