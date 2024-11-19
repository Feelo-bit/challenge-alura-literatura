package com.alura.challenge.literatura.dto;
import com.alura.challenge.literatura.model.Autor;


public record LibroDTO(        Long Id,
                               String titulo,
                               Autor autor,
                               String idioma,
                               Double descargas) {
}
