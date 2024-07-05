package com.alura.Literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name="autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String fechaDeNacimiento;
    private String fechaDeMuerte;

    @ManyToOne
    private Libro libro;

    public Autor() {}

    public Autor(DatosAutor autor) {
        this.nombre = autor.nombre();
        this.fechaDeNacimiento = autor.fechaDeNacimiento();
        this.fechaDeMuerte = autor.fachaDeMuerte();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getFechaDeMuerte() {
        return fechaDeMuerte;
    }

    public void setFechaDeMuerte(String fechaDeMuerte) {
        this.fechaDeMuerte = fechaDeMuerte;
    }

    public Libro getLibros() {
        return libro;
    }

    public void setLibros(Libro libro) {
        this.libro = libro;
    }

    @Override
    public String toString() {
        return "       Autor            \n" +
                "A;o de nacimiento: " + fechaDeNacimiento + "\n" +
                "A;o de muerte: " + fechaDeMuerte + "\n" +
                "Libros: " + libro;
    }

}
