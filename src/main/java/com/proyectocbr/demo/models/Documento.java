package com.proyectocbr.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_documento;

    private String titulo;
    private int anioPublicacion;
    @Lob
    @Column(name = "resumen", columnDefinition = "TEXT")
    private String resumen;


    private String contenido;

    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor autor;

    @ManyToOne
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;
}
