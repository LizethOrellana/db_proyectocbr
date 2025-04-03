package com.proyectocbr.demo.controller;

import com.proyectocbr.demo.models.Autor;
import com.proyectocbr.demo.models.Carrera;
import com.proyectocbr.demo.models.Documento;
import com.proyectocbr.demo.repository.AutorRepository;
import com.proyectocbr.demo.repository.CarreraRepository;
import com.proyectocbr.demo.repository.DocumentoRepository;
import com.proyectocbr.demo.service.AutorService;
import com.proyectocbr.demo.service.CarreraService;
import com.proyectocbr.demo.service.DocumentoService;
import com.proyectocbr.demo.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private DocumentoRepository documentoRepository;

    private static final Logger logger = LoggerFactory.getLogger(DocumentoController.class);
    @Autowired
    private AutorService autorService;
    @Autowired
    private CarreraService carreraService;
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private CarreraRepository carreraRepository;


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

    @PostMapping("/buscarAutor")
    public List<Documento> getDocumentoByAutor(@RequestBody Autor autor) {
        logger.info("Buscando documentos por autor");
        return documentoService.getDocumentoByAutor(autor);
    }

    @PostMapping("/buscarCarrera")
    public List<Documento> getDocumentoByCarrera(@RequestBody Carrera carrera) {
        logger.info("Buscando documentos por carrera");
        return documentoService.getDocumentoByCarrera(carrera);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Documento> updateDocumento(@PathVariable Long id, @RequestBody Documento documentoActualizado) {
        Documento documento = documentoService.updateDocumento(id, documentoActualizado);
        if (documento != null) {
            return ResponseEntity.ok(documento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Crear o actualizar documento
    @PostMapping("/crear")
    public ResponseEntity<String> createDocumento(
            @RequestParam("file") MultipartFile file,
            @RequestParam("titulo") String titulo,
            @RequestParam("resumen") String resumen,
            @RequestParam("anioPublicacion") int anioPublicacion,
            @RequestParam("autorId") Long autorId,
            @RequestParam("carreraId") Long carreraId) {
        try {
            Documento documento = new Documento();
            documento.setContenido(file.getBytes());
            documento.setTitulo(titulo);
            documento.setResumen(resumen);
            documento.setAnioPublicacion(anioPublicacion);

            // Obtener Autor y Carrera usando orElse()
            Autor autor = autorRepository.findById((long) autorId).orElse(null); // Devuelve null si no se encuentra
            Carrera carrera = carreraRepository.findById((long) carreraId).orElse(null); // Devuelve null si no se encuentra

            if (autor == null || carrera == null) {
                return ResponseEntity.badRequest().body("Autor o Carrera no encontrados");
            }

            documento.setAutor(autor);
            documento.setCarrera(carrera);

            documentoService.saveDocumento(documento);
            return ResponseEntity.ok("Documento guardado correctamente");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar documento: " + e.getMessage());
        }
    }

    @GetMapping("/verdocumento/{id}")
    public ResponseEntity<byte[]> getDocumento(@PathVariable Long id) {
        logger.info("Id enviado: " + id);
        Documento documento = documentoRepository.findById(id).orElse(null);
        if (documento != null && documento.getContenido() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF); // Ajusta el Content-Type según el tipo de archivo
            headers.setContentDisposition(ContentDisposition.inline().build()); // o .attachment().build() para descarga
            return new ResponseEntity<>(documento.getContenido(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscarPorAnio")
    public List<Documento> getDocumentosByAnio(@RequestParam int anio) {
        return documentoService.getDocumentosByAnio(anio);
    }

    @GetMapping("/buscarPorNombre")
    public List<Documento> getDocumentosByNombre(@RequestParam String nombre) {
        return documentoService.getDocumentosByNombre(nombre);
    }


    // Eliminar documento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumento(@PathVariable Long id) {
        documentoService.deleteDocumento(id);
        return ResponseEntity.noContent().build();
    }
}
