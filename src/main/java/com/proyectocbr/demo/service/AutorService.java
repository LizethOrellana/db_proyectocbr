package com.proyectocbr.demo.service;

import com.proyectocbr.demo.models.Autor;
import com.proyectocbr.demo.models.Carrera;
import com.proyectocbr.demo.repository.AutorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    // Obtener todos los autores
    public List<Autor> getAllAutores() {
        return autorRepository.findAll();
    }

    // Obtener autor por ID
    public Optional<Autor> getAutorById(Long id) {
        return autorRepository.findById(id);
    }


    public Autor saveAutor(Autor autor) {
        if (autor.getId_autor() != null && autorRepository.existsById(autor.getId_autor())) {
            throw new RuntimeException("El autor con ID " + autor.getId_autor() + " ya existe.");
        }
        return autorRepository.save(autor);
    }


    // Eliminar autor
    public void deleteAutor(Long id) {
        autorRepository.deleteById(id);
    }

    //Buscar autor
    public List<Autor> searchAutor(String nombre) {
        return autorRepository.findByNombre(nombre);
    }

}
