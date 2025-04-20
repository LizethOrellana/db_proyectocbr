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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Value("${file.upload.directory}")
    private String uploadDirectory;



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

    //Obtener el documento por el autor
    @PostMapping("/buscarAutor")
    public List<Documento> getDocumentoByAutor(@RequestBody Autor autor) {
        logger.info("Buscando documentos por autor");
        return documentoService.getDocumentoByAutor(autor);
    }

    //Obtener el documento por carrera
    @PostMapping("/buscarCarrera")
    public List<Documento> getDocumentoByCarrera(@RequestBody Carrera carrera) {
        logger.info("Buscando documentos por carrera");
        return documentoService.getDocumentoByCarrera(carrera);
    }

    //Actualizar documento
    @PutMapping("/{id}")
    public ResponseEntity<Documento> updateDocumento(@PathVariable Long id, @RequestBody Documento documentoActualizado) {
        Documento documento = documentoService.updateDocumento(id, documentoActualizado);
        if (documento != null) {
            return ResponseEntity.ok(documento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear documento
    @PostMapping("/crear")
    public ResponseEntity<String> createDocumento(
            @RequestParam(value = "file", required = false)  MultipartFile file,
            @RequestParam("titulo") String titulo,
            @RequestParam(value = "documentoId", required = false) String documentoId,
            @RequestParam(value = "contenido", required = false) String contenido,
            @RequestParam("resumen") String resumen,
            @RequestParam("anioPublicacion") int anioPublicacion,
            @RequestParam("autorId") Long autorId,
            @RequestParam("carreraId") Long carreraId) {
        try {
            logger.info("Creando Documento");
            Documento documento = new Documento();
            if (documentoId != null && documentoId.matches("\\d+")) {
                Long docId = Long.parseLong(documentoId);
                documento = documentoRepository.findById(docId).orElse(null);
                if (documento == null) {
                    return ResponseEntity.badRequest().body("Documento no encontrado");
                }
            }
            if(contenido == null) {
                // Generar un nombre de archivo único
                String originalFilename = file.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
                Path filePath = Paths.get(uploadDirectory, uniqueFilename);

                // Guardar el archivo en la carpeta
                file.transferTo(filePath);

                // Guardar la ruta del archivo en la base de datos
                documento.setContenido(filePath.toString());
            }else{
                documento.setContenido(contenido);
            }
            documento.setTitulo(titulo);
            documento.setResumen(resumen);
            documento.setAnioPublicacion(anioPublicacion);

            // Obtener Autor y Carrera usando orElse()
            Autor autor = autorRepository.findById(autorId).orElse(null);
            Carrera carrera = carreraRepository.findById(carreraId).orElse(null);

            if (autor == null || carrera == null) {
                return ResponseEntity.badRequest().body("Autor o Carrera no encontrados");
            }

            documento.setAutor(autor);
            documento.setCarrera(carrera);
            logger.info("documento resumen: "+documento.getResumen());
            logger.info("documento carrera: "+documento.getCarrera());
            logger.info("documento autor: "+documento.getAutor());
            logger.info("documento titulo: "+documento.getTitulo());
            logger.info("documento anio: "+documento.getAnioPublicacion());
            documentoService.saveDocumento(documento);
            return ResponseEntity.status(HttpStatus.OK).body("Documento guardado correctamente.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar documento: " + e.getMessage());
        }
    }

    //Metodo para mostrar el documento
    @GetMapping("/verdocumento/{id}")
    public ResponseEntity<Resource> getDocumento(@PathVariable Long id) {
        Documento documento = documentoRepository.findById(id).orElse(null);
        if (documento != null && documento.getContenido() != null) {
            Path filePath = Paths.get(documento.getContenido());
            Resource resource = new FileSystemResource(filePath);

            if (resource.exists()) {
                HttpHeaders headers = new HttpHeaders();
                MediaType mediaType;
                try {
                    mediaType = MediaType.parseMediaType(Files.probeContentType(filePath));
                } catch (IOException e) {
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                }
                headers.setContentType(mediaType);
                headers.setContentDisposition(ContentDisposition.inline().filename(filePath.getFileName().toString()).build());

                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //Obtener el documento por año
    @GetMapping("/buscarPorAnio")
    public List<Documento> getDocumentosByAnio(@RequestParam int anio) {
        return documentoService.getDocumentosByAnio(anio);
    }

    //Obtener el documento por nombre
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
