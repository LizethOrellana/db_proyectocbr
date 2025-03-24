package com.proyectocbr.demo.service;

import com.proyectocbr.demo.models.Carrera;
import com.proyectocbr.demo.repository.CarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarreraService {

    @Autowired
    private CarreraRepository carreraRepository;

    // Obtener todas las carreras
    public List<Carrera> getAllCarreras() {
        return carreraRepository.findAll();
    }

    // Obtener carrera por ID
    public Optional<Carrera> getCarreraById(Long id) {
        return carreraRepository.findById(id);
    }

    // Crear o actualizar carrera
    public Carrera saveCarrera(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    // Eliminar carrera
    public void deleteCarrera(Long id) {
        carreraRepository.deleteById(id);
    }
}