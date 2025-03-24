package com.proyectocbr.demo.service;

import com.proyectocbr.demo.models.Documento;
import com.proyectocbr.demo.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    // Obtener todos los documentos
    public List<Documento> getAllDocumentos() {
        return documentoRepository.findAll();
    }

    // Obtener documento por ID
    public Optional<Documento> getDocumentoById(Long id) {
        return documentoRepository.findById(id);
    }

    // Crear o actualizar documento
    public Documento saveDocumento(Documento documento) {
        return documentoRepository.save(documento);
    }

    // Eliminar documento
    public void deleteDocumento(Long id) {
        documentoRepository.deleteById(id);
    }
}