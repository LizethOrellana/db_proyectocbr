package com.proyectocbr.demo.controller;

import com.proyectocbr.demo.models.Autor;
import com.proyectocbr.demo.repository.AutorRepository;
import com.proyectocbr.demo.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/autores")
public class AutorController {
    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorService autorService;

    // Obtener todos los autores
    @GetMapping
    public List<Autor> getAllAutores() {
        return autorService.getAllAutores();
    }

    // Obtener autor por ID
    @GetMapping("/{id}")
    public Optional<Autor> getAutorById(@PathVariable Long id) {
        return autorService.getAutorById(id);
    }

    // Eliminar autor
    @DeleteMapping("/{id}")
    public void deleteAutor(@PathVariable Long id) {
        autorService.deleteAutor(id);
    }

    // Crear o actualizar autor
    @PostMapping("/crearautor")
    public Autor createAutor(@RequestBody Autor autor) {
        return autorService.saveAutor(autor);
    }
}
