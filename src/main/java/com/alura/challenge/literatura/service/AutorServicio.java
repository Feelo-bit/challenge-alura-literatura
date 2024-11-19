package com.alura.challenge.literatura.service;

import com.alura.challenge.literatura.dto.AutorDTO;
import com.alura.challenge.literatura.model.Autor;
import com.alura.challenge.literatura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AutorServicio {
    @Autowired
    private AutorRepository repository;

    // Método para obtener todos los autores y convertirlos en DTO
    public List<AutorDTO> obtenerTodosLosAutores() {
        // Obtiene todos los autores de la base de datos utilizando el repositorio
        List<Autor> autores = repository.findAll();
        // Convierte la lista de autores en una lista de DTO de autores utilizando el método convierteDatos
        return convierteDatos(autores);
    }

    // Método para convertir una lista de autores en una lista de DTO de autores
    public List<AutorDTO> convierteDatos(List<Autor> autores) {
        // Utiliza stream y map para convertir cada autor en un DTO de autor y luego los recopila en una lista
        return autores.stream()
                .map(a -> new AutorDTO(
                        a.getId(),
                        a.getNombre(),
                        a.getFechaNacimiento(),
                        a.getFechaFallecimiento()
                ))
                .collect(Collectors.toList());
    }
}
