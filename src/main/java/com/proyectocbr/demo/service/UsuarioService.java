package com.proyectocbr.demo.service;

import com.proyectocbr.demo.models.Usuario;
import com.proyectocbr.demo.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario saveUsuario(Usuario usuario) {
        logger.info("Usuario");
        logger.info("Nombre:"+usuario.getNombre());
        logger.info("Cedula:"+usuario.getCedula());
        logger.info("Rol:"+usuario.getRol());
        logger.info("Primera Pregunta:"+usuario.getPrimera_pregunta());
        logger.info("Segunda Pregunta:"+usuario.getPrimera_pregunta());
        //logger.info("201 Created: Usuario registrado correctamente");

        return usuarioRepository.save(usuario);
    }
}
