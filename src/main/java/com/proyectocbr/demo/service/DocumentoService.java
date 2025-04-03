package com.proyectocbr.demo.service;

import com.proyectocbr.demo.models.Autor;
import com.proyectocbr.demo.models.Carrera;
import com.proyectocbr.demo.models.Documento;
import com.proyectocbr.demo.repository.DocumentoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    // Obtener todos los documentos
    public List<Documento> getAllDocumentos() {
        return documentoRepository.findAll();
    }

    public List <Documento> getDocumentoByAutor(Autor autor){return documentoRepository.findByAutor(autor);}

    public List <Documento> getDocumentoByCarrera(Carrera carrera){return documentoRepository.findByCarrera(carrera);}

    // Obtener documento por ID
    public Optional<Documento> getDocumentoById(Long id) {
        return documentoRepository.findById(id);
    }

    // Crear o actualizar documento
    @Transactional
    public Documento saveDocumento(Documento documento) {
        logger.info("ID DOCUMENTO:"+documento.getId_documento());
        return documentoRepository.save(documento);
    }

    public Documento updateDocumento(Long id, Documento documentoActualizado) {
        Documento documentoExistente = documentoRepository.findById(id).orElse(null);
        if (documentoExistente != null) {
            documentoActualizado.setId_documento(id);
            return documentoRepository.save(documentoActualizado);
        } else {
            return null;
        }
    }

    // Eliminar documento
    public void deleteDocumento(Long id) {
        documentoRepository.deleteById(id);
    }

    public List<Documento> getDocumentosByAnio(int anio) {
        return documentoRepository.findByAnioPublicacion(anio);
    }

    public List<Documento> getDocumentosByNombre(String nombre) {
        return documentoRepository.findByTitulo(nombre);
    }
}