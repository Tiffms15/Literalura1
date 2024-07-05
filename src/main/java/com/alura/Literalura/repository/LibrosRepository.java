package com.alura.Literalura.repository;

import com.alura.Literalura.model.Libro;
import com.alura.Literalura.model.Idioma;
import com.alura.Literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibrosRepository extends JpaRepository<Libro,Long>{
    List<Libro> findByIdiomas(Idioma idioma);

    @Query("SELECT l FROM Libro a JOIN a.autor l")
    List<Autor> mostrarAutores();

    @Query("SELECT l FROM Libro a JOIN a.autor l WHERE l.fechaDeNacimiento <= :anio AND l.fechaDeMuerte>= :anio")
    List<Autor> mostrarAutoresVivos(String anio);


}