package com.alura.Literalura.principal;

import com.alura.Literalura.model.*;
import com.alura.Literalura.repository.LibrosRepository;
import com.alura.Literalura.service.*;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Scanner;
import java.util.*;


public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private LibrosRepository repositorio;

    public Principal (LibrosRepository repository){
        this.repositorio=repository;
    }

    public void muestraElMenu() {

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    -------------------------------------------
                    Elija la opcion a traves de su numero:
                    1- Buscar libro por titulo
                    2- Listar resultados registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar resultados por idioma
                    0- Salir
                    ----------------------------------------------
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    librosRegistrados();
                    break;
                case 3:
                    autoresRegistrados();
                    break;
                case 4:
                    autoresVivosAnnio();
                    break;
                case 5:
                    librosIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicacion...");
                    break;
                default:
                    System.out.println("Opcion Invalida");
            }
        }
    }

    private DatosLibros buscarLibro() {
        System.out.println("Ingresa el nombre del libro a buscar en la Web");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println("Libro encontrado...");
            return libroBuscado.get();

        } else {
            System.out.println("libro no encontrado, intenta con otro título\n");
            return null;
        }

    }
    Optional<DatosLibros> datosOpcional = Optional.ofNullable(buscarLibro());
    private void guardarLibros(DatosLibros datosLibros) {
        if(datosOpcional.isPresent()) {
            DatosLibros datos = datosOpcional.get();

            Libro libro = new Libro(datos);
            List<Autor> autores = new ArrayList<>();
            for (DatosAutor datosAutor : datos.autor()) {
                Autor autor = new Autor(datosAutor);
                autor.setLibros(libro);
                autores.add(autor);
            }
            libro.setAutor(autores);
            try {
                repositorio.save(libro);
                System.out.println(libro.getTitulo() + " guardado exitosamente!!!");
            } catch (DataIntegrityViolationException e) {
                System.out.println("Error: libro ya está almacenado en la base de datos, intenta con otro libro.\n");
            }
        }
    }

    private DatosLibros buscarTitulo(String tituloLibro)throws IOException {
        String json =consumoAPI.obtenerDatos(URL_BASE+"?search=" + tituloLibro.replace(" ","+"));
        Datos datosBusqueda = conversor.obtenerDatos(json,Datos.class);

        Optional <DatosLibros> librosBuscados = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        return librosBuscados.orElse(null);
//        Libros resultados = new Libros(datos);
//        repositorio.save(resultados);
       // System.out.println(datos);

    }

    private void autoresVivosAnnio() {
        System.out.println("Ingrese el año que desea buscar: ");
        String fecha = teclado.nextLine();
        List<Autor> autoresVivos = repositorio.mostrarAutoresVivos(fecha);
        if (autoresVivos.isEmpty()) {
            System.out.println("No hay autores del año: " + fecha);
            return;
        }
        Map<String, List<String>> autoresLibros = autoresVivos.stream()
                .collect(Collectors.groupingBy(
                        Autor::getNombre,
                        Collectors.mapping(a -> a.getLibros().getTitulo(),Collectors.toList())
                ));
        autoresLibros.forEach((nombre, libros) -> {
            Autor autor = autoresVivos.stream()
                    .filter(a -> a.getNombre().equals(nombre))
                    .findFirst().orElse(null);
            if (autor != null) {
                System.out.println("------------------------------");
                System.out.println("           AUTOR              ");
                System.out.println("------------------------------");
                System.out.println("Nombre: " + nombre);
                System.out.println("Fecha de nacimiento: " + autor.getFechaDeNacimiento());
                System.out.println("Fecha de muerte: " + autor.getFechaDeMuerte());
                System.out.println("Libros: " + libros + "\n");
            }
        });
    }

    private void autoresRegistrados() {
        List<Autor> mostarListaAutores = repositorio.mostrarAutores();

        Map<String, List<String>> autoresConLibros = mostarListaAutores.stream()
                .collect(Collectors.groupingBy(
                        Autor::getNombre,
                        Collectors.mapping(a -> a.getLibros().getTitulo(), Collectors.toList())
                ));

        autoresConLibros.forEach((nombre, libros) -> {
            Autor autor = mostarListaAutores.stream()
                    .filter(a -> a.getNombre().equals(nombre))
                    .findFirst().orElse(null);
            if (autor != null) {
                System.out.println("------------------------------");
                System.out.println("           AUTOR              ");
                System.out.println("------------------------------");
                System.out.println("Nombre: " + nombre);
                System.out.println("Fecha de nacimiento: " + autor.getFechaDeNacimiento());
                System.out.println("Fecha de muerte: " + autor.getFechaDeMuerte());
                System.out.println("Libros: " + libros + "\n");
            }
        });
    }

    private void librosRegistrados() {
        List<Libro> libro = repositorio.findAll();
        if (libro.isEmpty()) {
            System.out.println("No hay resultados regustrados");
        }else
            System.out.println("--------------------------");
        System.out.println("Libros registrados: ");
        System.out.println("--------------------------");
        libro.forEach(l -> System.out.println(
                "--------------------------"+
                "          LIBRO          " +
                        "--------------------------"+
                        "\nTítulo: " + l.getTitulo()+
                        "\nIdioma: " + l.getIdiomas()+
                        "\nAutor: " + l.getAutor().stream().map(Autor::getNombre).collect(Collectors.joining()) +
                        "\nNúmero de descargas: " + l.getNumeroDescargas() +
                        "\n"
        ));
            }


//        resultados.stream()
//                .sorted(Comparator.comparing(Libros::getAutor))
//                .forEach(System.out::println);
//    }
    private void librosIdioma (){
        System.out.println("""
            -----------------------------------------
            Selecione el lenguaje"
            ----------------------------------------
                    EN-Ingles
                    ES-Espa;ol
                    FR-Frances
                    PR-Portugues
                    0. Volver al menu anterior
            """);
        var opcion= teclado.nextLine();
        try {
            List<Libro> libroIdioma = repositorio.findByIdiomas(Idioma.valueOf(opcion.toUpperCase()));
            libroIdioma.forEach(n -> System.out.println(
                    "----------------------------------------: " +
                    "          LIBRO          " +
                            "\n------------------------------------- " +
                            "\nTitulo: " + n.getTitulo() +
                            "\nIndioma: " + n.getIdiomas() +
                            "\nAutor: " + n.getAutor().stream().map(Autor::getNombre).collect(Collectors.joining()) +
                            "\nNumero de descargas: " + n.getNumeroDescargas() +
                            "\n"
            ));
        } catch (IllegalArgumentException e){
            System.out.println("Idioma no existe...\n");

            }
        }

}
//System.out.println("Ingrese el nombre del libro: ");
//        String tituloLibro = teclado.nextLine();
//        DatosLibros libros = null;
//        try {
//            libros= buscarTitulo(tituloLibro);
//        }catch (IOException e){
//            throw new RuntimeException(e);
//        }
//        if (libros!=null) {
//            guardarLibros(libros);
//            System.out.println("Libro encontrado: ");
//            System.out.println("Titulo: " + libros.titulo());
//            System.out.println("Autor: " + libros.autor());
//            System.out.println("Idioma: " + libros.idiomas());
//            System.out.println("Numero de descargas: " + libros.numeroDeDescargas());
//        }else {
//            System.out.println("Libro no encontrado");
//        }