package com.proyectocbr.demo.controller;

import com.proyectocbr.demo.models.Documento;
import com.proyectocbr.demo.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    // Obtener todos los documentos
    @GetMapping
    public List<Documento> getAllDocumentos() {
        return documentoService.getAllDocumentos();
    }

    // Obtener documento por ID
    @GetMapping("/{id}")
    public Optional<Documento> getDocumentoById(@PathVariable Long id) {
        return documentoService.getDocumentoById(id);
    }

    // Crear o actualizar documento
    @PostMapping
    public Documento createDocumento(@RequestBody Documento documento) {
        return documentoService.saveDocumento(documento);
    }

    // Eliminar documento
    @DeleteMapping("/{id}")
    public void deleteDocumento(@PathVariable Long id) {
        documentoService.deleteDocumento(id);
    }
}
