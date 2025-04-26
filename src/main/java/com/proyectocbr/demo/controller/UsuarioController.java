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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    //Obtener todos los usuarios
    @GetMapping
    public List<Usuario> getUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // Crear usuario
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
            logger.info("Iniciando Sesión");
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

    //Modificar el usuario
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
            usuarioExistente.setPrimera_pregunta(usuarioActualizado.getPrimera_pregunta());
            usuarioExistente.setSegunda_pregunta(usuarioActualizado.getSegunda_pregunta());
            usuarioExistente.setContrasenia(usuarioActualizado.getContrasenia());

            usuarioService.saveUsuario(usuarioExistente); // Guarda el usuario actualizado
            return ResponseEntity.ok("Usuario actualizado correctamente");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al editar usuario: " + e.getMessage());
        }
    }

    //Eliminar usuario
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

    //buscar usuario por cedula
    @GetMapping("/{cedula}")
    public ResponseEntity<?> obtenerUsuarioPorCedula(@PathVariable String cedula) {
        Usuario usuario = usuarioService.obtenerUsuarioPorCedula(cedula);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(usuario);
    }

    //Metodo para verificar las preguntas de seguridad
    @PostMapping("/verificarPreguntas/{cedula}")
    public ResponseEntity<?> verificarPreguntasSeguridad(
            @PathVariable String cedula,
            @RequestBody Map<String, String> respuestas) {

        Usuario usuarioExistente = usuarioService.obtenerUsuarioPorCedula(cedula);
        if (usuarioExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        String primeraPregunta = respuestas.get("primeraPregunta"); //cambiado para que coincida con el json
        String segundaPregunta = respuestas.get("segundaPregunta");//cambiado para que coincida con el json

        if (primeraPregunta == null || segundaPregunta == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Faltan preguntas de seguridad");
        }

        if (usuarioExistente.getPrimera_pregunta().equalsIgnoreCase(primeraPregunta) &&
                usuarioExistente.getSegunda_pregunta().equalsIgnoreCase(segundaPregunta)) {
            return ResponseEntity.ok("Preguntas de seguridad correctas");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Respuestas de seguridad incorrectas");
        }
    }

    // Metodo para modificar la contraseña del usuario
    @PutMapping("/cambiarContrasenia/{cedula}")
    public ResponseEntity<?> cambiarContrasenia(@PathVariable String cedula, @RequestBody Map<String, String> nuevaContrasenia) {
        try {
            Usuario usuarioExistente = usuarioService.obtenerUsuarioPorCedula(cedula);
            if (usuarioExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            String contrasenia = nuevaContrasenia.get("nuevaContrasenia"); //manejo del json
            if(contrasenia == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta la nueva contraseña");
            }
            usuarioExistente.setContrasenia(contrasenia); // ¡Asegúrate de hashear esto!
            usuarioService.saveUsuario(usuarioExistente);
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la contraseña: " + e.getMessage());
        }
    }
}
