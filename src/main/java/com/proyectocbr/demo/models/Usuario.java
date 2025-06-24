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

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String contrasenia;

    @Column(nullable = false, length = 10, unique = true)
    private String cedula;

    @Column(nullable = false, length = 20)
    private String rol;

    @Column(nullable = false, length = 100)
    private String primera_pregunta;

    @Column(nullable = false, length = 100)
    private String segunda_pregunta;
}
