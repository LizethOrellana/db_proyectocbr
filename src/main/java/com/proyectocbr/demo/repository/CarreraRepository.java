package com.proyectocbr.demo.repository;

import com.proyectocbr.demo.models.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    List<Carrera> findByNombreContaining(String nombre);
}
