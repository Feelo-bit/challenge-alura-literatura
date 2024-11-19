package com.alura.challenge.literatura.principal;


import com.alura.challenge.literatura.model.Autor;
import com.alura.challenge.literatura.model.DatosAutor;
import com.alura.challenge.literatura.model.DatosLibro;
import com.alura.challenge.literatura.model.Libro;
import com.alura.challenge.literatura.repository.AutorRepository;
import com.alura.challenge.literatura.repository.LibroRepository;
import com.alura.challenge.literatura.service.ConsumoAPI;
import com.alura.challenge.literatura.service.ConvierteDatos;
import com.alura.challenge.literatura.service.ConvierteDatosAutor;

import java.util.*;
import java.util.stream.Collectors;

// Clase principal
public class Principal {
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private ConvierteDatosAutor conversorAutor = new ConvierteDatosAutor();
    private final String URL_BASE = "https://gutendex.com/books/";
    private Scanner teclado = new Scanner(System.in);
    private LibroRepository libRepositorio;
    private AutorRepository autorRepository;
    private List<Libro> libros;
    private List<Autor> autores;
    private Optional<Autor> autorBuscado;

    public Principal(LibroRepository libRepository, AutorRepository autRepository) {
        this.libRepositorio = libRepository;
        this.autorRepository = autRepository;
    }

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            try {
                mostrarMenu();
                opcion = obtenerOpcionDelUsuario();
                procesarOpcion(opcion);
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número del 0 al 10.");
                teclado.nextLine(); // Limpiar el buffer del scanner
            }
        }
        System.out.println("Cerrando la aplicación...");
        System.exit(0);
    }

    // Método para mostrar el menú en consola
    private void mostrarMenu() {
        var menu = """
                -----------------------------------------------------------------------------                 
                -----------------------------------------------------------------------------
                Elija la opción a través de su número
                1- Consultar libro por titulo 
                2- Listar libros registrados 
                3- Listar autores registrados
                4- Buscar autores vivos en un determinado año de la BD
                5- Buscar libros registrados en la BD por idioma
                0 - Salir
            """;
        System.out.println(menu);
    }

    // Método para obtener la opción del usuario
    private int obtenerOpcionDelUsuario() {
        System.out.print("Ingrese su opción: ");
        return teclado.nextInt();
    }

    // Método para procesar la opción del usuario
    private void procesarOpcion(int opcion) {
        teclado.nextLine();
        System.out.println();
        switch (opcion) {
            case 1:
                buscarYGuardarLibroAPI();
                break;
            case 2:
                mostrarLibrosBaseDatos();
                break;
            case 3:
                mostrarAutoresBaseDatos();
                break;
            case 4:
                mostrarAutoresVivosEnUnDeterminadoAno();
                break;
            case 5:
                mostrarLibrosPorIdioma();
                break;
            case 0:
                break;
            default:
                System.out.println("Opción inválida. Por favor, intente nuevamente.");
        }
        System.out.println();
    }

    // Método para obtener datos de un libro desde la API
    private DatosLibro obtenerDatosLibroAPI(String nombreLibro) {
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        return conversor.obtenerDatos(json, DatosLibro.class);
    }

    // Método para obtener datos de un autor desde la API
    private DatosAutor obtenerDatosAutorAPI(String nombreLibro) {
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        return conversorAutor.obtenerDatos(json, DatosAutor.class);
    }

    // Método para buscar y guardar un libro desde la API
    public void buscarYGuardarLibroAPI() {
        System.out.println("¿Cuál es el título del libro que desea buscar?");
        String libroBuscado = teclado.nextLine();
        libros = libros != null ? libros : new ArrayList<>();
        Optional<Libro> optionalLibro = libros.stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(libroBuscado.toLowerCase()))
                .findFirst();
        if (optionalLibro.isPresent()) {
            var libroEncontrado = optionalLibro.get();
            System.out.println("Este libro ya ha sido cargado previamente:");
            System.out.println(libroEncontrado);
            System.out.println("Por favor, pruebe con otro título.");
        } else {
            try {
                DatosLibro datosLibro = obtenerDatosLibroAPI(libroBuscado);
                if (datosLibro != null) {
                    DatosAutor datosAutor = obtenerDatosAutorAPI(libroBuscado);
                    if (datosAutor != null) {
                        List<Autor> autores = autorRepository.findAll();
                        autores = autores != null ? autores : new ArrayList<>();
                        Optional<Autor> autorOptional = autores.stream()
                                .filter(a -> datosAutor.nombre() != null &&
                                        a.getNombre().toLowerCase().contains(datosAutor.nombre().toLowerCase()))
                                .findFirst();
                        Autor autor;
                        if (autorOptional.isPresent()) {
                            autor = autorOptional.get();
                        } else {
                            autor = new Autor(
                                    datosAutor.nombre(),
                                    datosAutor.fechaNacimiento(),
                                    datosAutor.fechaFallecimiento()
                            );
                            autorRepository.save(autor);
                        }
                        Libro libro = new Libro(
                                datosLibro.titulo(),
                                autor,
                                datosLibro.idioma() != null ? datosLibro.idioma() : Collections.emptyList(),
                                datosLibro.descargas()
                        );
                        libros.add(libro);
                        autor.setLibros(libros);
                        System.out.println("Se ha encontrado el siguiente libro:");
                        System.out.println(libro);
                        libRepositorio.save(libro);
                        System.out.println("El libro ha sido guardado exitosamente.");
                    } else {
                        System.out.println("No se encontró información sobre el autor para este libro.");
                    }
                } else {
                    System.out.println("No se encontró el libro con el título proporcionado.");
                }
            } catch (Exception e) {
                System.out.println("Se produjo una excepción: " + e.getMessage());
            }
        }
    }

    // Método para mostrar libros de la base de datos
    private void mostrarLibrosBaseDatos() {
        System.out.println("Libros registrados");
        try {
            List<Libro> libros = libRepositorio.findAll();
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getDescargas))
                    .forEach(System.out::println);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            libros = new ArrayList<>();
        }
    }

    public void mostrarAutoresBaseDatos() {
        System.out.println("Autores registrados");
        autores = autorRepository.findAll();
        autores.stream()
                .forEach(System.out::println);
    }

    public void mostrarAutoresVivosEnUnDeterminadoAno() {
        System.out.println("Búsqueda de autores vivos en un año especifico");
        System.out.print("Ingrese un año: ");
        int anio = teclado.nextInt();
        List<Autor> autores = autorRepository.findAll();
        List<String> autoresNombre = autores.stream()
                .filter(a -> a.getFechaFallecimiento() >= anio && a.getFechaNacimiento() <= anio)
                .map(Autor::getNombre)
                .collect(Collectors.toList());

        if (autoresNombre.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio);
        } else {
            System.out.println("Autores vivos en el año " + anio + ":");
            autoresNombre.forEach(System.out::println);
        }
    }

    // Método para mostrar libros por idioma desde la base de datos
    public void mostrarLibrosPorIdioma() {
        libros = libRepositorio.findAll();
        System.out.println("Búsqueda de libros registrados en la BD por idioma");
        System.out.println("Ingrese el idioma del que desea buscar los libros: en (english) o es (español)");
        String idiomaBuscado = teclado.nextLine();
        List<Libro> librosBuscados = libros.stream()
                .filter(l -> l.getIdioma().contains(idiomaBuscado))
                .collect(Collectors.toList());
        librosBuscados.forEach(System.out::println);
    }
}
