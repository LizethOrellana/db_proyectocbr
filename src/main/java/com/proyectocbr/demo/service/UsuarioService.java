package com.proyectocbr.demo.service;

import com.proyectocbr.demo.models.Usuario;
import com.proyectocbr.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Obtener todos los usuarios
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    //Guardar el usuario
    public Usuario saveUsuario(Usuario usuario) {
        logger.info("Usuario");
        logger.info("Nombre:"+usuario.getNombre());
        logger.info("Cedula:"+usuario.getCedula());
        logger.info("Rol:"+usuario.getRol());
        logger.info("Primera Pregunta:"+usuario.getMascota());
        logger.info("Segunda Pregunta:"+usuario.getCiudad());
        //logger.info("201 Created: Usuario registrado correctamente");

        return usuarioRepository.save(usuario);
    }

    //Obtener usuario por cedula
    public Usuario obtenerUsuarioPorCedula(String cedula) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByCedula(cedula);
        return usuarioOptional.orElse(null); // Devuelve el usuario si existe, o null si no existe
    }

    //Eliminar por usuario
    @Transactional
    public void eliminarUsuario(String cedula) {
        usuarioRepository.deleteByCedula(cedula);
    }
}
