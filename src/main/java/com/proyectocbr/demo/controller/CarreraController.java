package com.proyectocbr.demo.controller;

import com.proyectocbr.demo.models.Carrera;
import com.proyectocbr.demo.service.CarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    @Autowired
    private CarreraService carreraService;

    // Obtener todas las carreras
    @GetMapping
    public List<Carrera> getAllCarreras() {
        return carreraService.getAllCarreras();
    }

    // Obtener carrera por ID
    @GetMapping("/{id}")
    public Optional<Carrera> getCarreraById(@PathVariable Long id) {
        return carreraService.getCarreraById(id);
    }

    // Crear o actualizar carrera
    @PostMapping("/crear")
    public Carrera createCarrera(@RequestBody Carrera carrera) {
        return carreraService.saveCarrera(carrera);
    }

    // Eliminar carrera
    @DeleteMapping("/{id}")
    public void deleteCarrera(@PathVariable Long id) {
        carreraService.deleteCarrera(id);
    }

    //Obtener documento por nombre
    @GetMapping("/buscar")
    public List<Carrera> buscarCarreras(@RequestParam("nombre") String nombre) {
        return carreraService.searchCarrera(nombre);
    }
}
