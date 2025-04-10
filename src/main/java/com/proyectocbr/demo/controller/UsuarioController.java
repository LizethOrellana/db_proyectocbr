package com.proyectocbr.demo.controller;

import com.proyectocbr.demo.models.Usuario;
import com.proyectocbr.demo.repository.UsuarioRepository;
import com.proyectocbr.demo.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @GetMapping
    public List<Usuario> getUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @PostMapping("/crearusuario")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            usuarioService.saveUsuario(usuario);
            return ResponseEntity.ok("Usuario creado correctamente");
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return ResponseEntity.badRequest().body("Error: La cédula ya existe");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear usuario: " + e.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear usuario: " + e.getMessage());
        }
    }

    @PostMapping("/iniciarsesion")
    public ResponseEntity<?> iniciarSesion(@RequestParam String cedula, @RequestParam String contrasenia) {
        try {
            Optional<Usuario> usuarioEncontrado = usuarioRepository.findByCedula(cedula);

            if (usuarioEncontrado.isPresent()) {
                Usuario usuarioDB = usuarioEncontrado.get();
                if (usuarioDB.getContrasenia().equals(contrasenia)) {
                    return ResponseEntity.ok(usuarioDB);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al iniciar sesión: " + e.getMessage());
        }
    }

    @PutMapping("/editarusuario/{cedula}")
    public ResponseEntity<?> editarUsuario(@PathVariable String cedula, @RequestBody Usuario usuarioActualizado) {
        try {
            Usuario usuarioExistente = usuarioService.obtenerUsuarioPorCedula(cedula); // Implementa este método en tu servicio

            if (usuarioExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            // Actualiza los campos necesarios
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setRol(usuarioActualizado.getRol());
            usuarioExistente.setMascota(usuarioActualizado.getMascota());
            usuarioExistente.setCiudad(usuarioActualizado.getMascota());

            usuarioService.saveUsuario(usuarioExistente); // Guarda el usuario actualizado
            return ResponseEntity.ok("Usuario actualizado correctamente");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al editar usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/eliminarusuario/{cedula}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String cedula) {
        try {
            usuarioService.eliminarUsuario(cedula);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar usuario: " + e.getMessage());
        }
    }


    @PostMapping("/buscarusuario")
    public ResponseEntity<?> buscarUsuario(@RequestParam String cedula, @RequestParam String mascota, @RequestParam String ciudad) {
        try {
            // Busca al usuario que cumpla con los criterios en la base de datos
            Optional<Usuario> usuarioEncontrado = usuarioRepository.findByCedulaAndMascotaAndCiudad(cedula, mascota, ciudad);

            if (usuarioEncontrado.isPresent()) {
                // Si se encuentra, devuelve los datos del usuario
                return ResponseEntity.ok(usuarioEncontrado.get());
            } else {
                // Si no se encuentra, devuelve un mensaje de error
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con los datos proporcionados.");
            }
        } catch (Exception e) {
            // Manejo de errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar usuario: " + e.getMessage());
        }
    }

}

