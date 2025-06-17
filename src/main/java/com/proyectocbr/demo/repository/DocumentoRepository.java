package com.proyectocbr.demo.repository;

import com.proyectocbr.demo.models.Autor;
import com.proyectocbr.demo.models.Carrera;
import com.proyectocbr.demo.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByNombreContainingIgnoreCase(String nombre);
    List<Documento> findByAutor(Autor autor);
    List<Documento> findByCarrera(Carrera carrera);
    List<Documento> findByAnioPublicacion(int anioPublicacion);
    List<Documento> findByTitulo(String titulo); // Cambiar a findByTitulo si la propiedad se llama titulo
}
