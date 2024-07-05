package com.alura.Literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table (name = "libros")

public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idioma idiomas;
    @OneToMany(mappedBy = "libro",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Autor> autor;
    private Double numeroDescargas;

    public Libro(){}

    public Libro(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.idiomas = Idioma.toString(datosLibros.idiomas().stream().limit(1).collect(Collectors.joining()));
        this.numeroDescargas = datosLibros.numeroDeDescargas();

    }
    @Override
    public String toString() {
        return
                "Libro {" +
                        "id=" + id +
                        "titulo='" + titulo + '\'' +
                        ", autor='" + autor + '\'' +
                        " idioma='" + idiomas + '\'' +
                        ", numero de descargas='" + numeroDescargas + '\'';


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Idioma idiomas) {
        this.idiomas = idiomas;
    }

    public List<Autor> getAutor() {
        return autor;
    }

    public void setAutor(List<Autor> autor) {
        autor.forEach(a -> a.setLibros(this));
        this.autor = autor;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }
}