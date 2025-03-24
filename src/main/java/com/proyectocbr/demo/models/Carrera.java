package com.proyectocbr.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carreras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_carrera;

    private String nombre;
}
