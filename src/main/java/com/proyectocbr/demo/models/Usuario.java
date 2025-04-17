package com.proyectocbr.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false, unique = true)
    private int id_usuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String contrasenia;

    @Column(nullable = false)
    private String cedula;

    //ROLES: ADMIN, CONTRIBUIDOR, PUBLICO
    @Column(nullable = false)
    private String rol;

    @Column(nullable = false)
    private String mascota;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String primera_pregunta;

    @Column(nullable = false)
    private String segunda_pregunta;
}
