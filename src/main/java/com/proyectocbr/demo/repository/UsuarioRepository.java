package com.proyectocbr.demo.repository;

import com.proyectocbr.demo.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCedula(String cedula);

    void deleteByCedula(String cedula);

    Optional<Usuario> findByCedulaAndMascotaAndCiudad(String cedula, String mascota, String ciudad);

    String ciudad(String ciudad);
}
