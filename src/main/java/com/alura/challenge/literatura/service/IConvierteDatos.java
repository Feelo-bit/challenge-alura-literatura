package com.alura.challenge.literatura.service;

import java.util.List;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
    <T> List<T> obtenerDatosArray(String json, Class<T> clase);
}
