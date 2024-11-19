# README - Proyecto de Gestión de Libros y Autores

## Descripción
Este proyecto permite consultar y gestionar libros y autores a través de una interfaz de consola. La aplicación interactúa con una API externa (Gutendex) para obtener información sobre libros y autores, almacenando estos datos en una base de datos local. Los usuarios pueden buscar libros por título, listar libros y autores registrados, filtrar autores vivos en un año específico, y buscar libros por idioma.

## Requisitos

- Java 17 o superior.
- Dependencias:
  - `Spring Data JPA` para la interacción con la base de datos.
  - `Gson` para la conversión de JSON a objetos Java.
  - `Scanner` para la entrada de datos desde consola.

## Clases Principales

### 1. `Principal`

Clase principal que maneja la interacción con el usuario y las opciones del menú. Esta clase incluye métodos para:

- Consultar un libro por su título.
- Listar los libros y autores registrados en la base de datos.
- Buscar autores vivos en un año específico.
- Filtrar libros registrados por idioma.

### Métodos principales de la clase `Principal`:

- `muestraElMenu()`: Muestra el menú principal y gestiona las opciones ingresadas por el usuario.
- `obtenerOpcionDelUsuario()`: Obtiene la opción seleccionada por el usuario.
- `procesarOpcion(int opcion)`: Procesa la opción seleccionada por el usuario y ejecuta la acción correspondiente.
- `buscarYGuardarLibroAPI()`: Busca un libro en la API de Gutendex por título y lo guarda en la base de datos.
- `mostrarLibrosBaseDatos()`: Muestra todos los libros registrados en la base de datos, ordenados por el número de descargas.
- `mostrarAutoresBaseDatos()`: Muestra todos los autores registrados en la base de datos.
- `mostrarAutoresVivosEnUnDeterminadoAno()`: Muestra los autores vivos en un año específico proporcionado por el usuario.
- `mostrarLibrosPorIdioma()`: Muestra los libros registrados en la base de datos filtrados por idioma.

### 2. `ConsumoAPI`

Clase que se encarga de realizar solicitudes HTTP a la API de Gutendex y obtener los datos en formato JSON.

### 3. `ConvierteDatos` y `ConvierteDatosAutor`

Estas clases son responsables de convertir los datos JSON obtenidos de la API a objetos Java utilizando la librería `Gson`.

### 4. `LibroRepository` y `AutorRepository`

Interfaces de repositorios que interactúan con la base de datos para almacenar y recuperar objetos `Libro` y `Autor`.

## Flujo de la Aplicación

1. **Consulta de libro por título**: El usuario ingresa el título del libro y la aplicación consulta la API de Gutendex para obtener los detalles del libro y del autor, los cuales se almacenan en la base de datos si no se encuentran previamente registrados.

2. **Listar libros y autores registrados**: El usuario puede consultar todos los libros y autores almacenados en la base de datos.

3. **Buscar autores vivos en un año específico**: El usuario puede ingresar un año y la aplicación muestra los autores que están vivos en ese año, basándose en las fechas de nacimiento y fallecimiento.

4. **Buscar libros por idioma**: El usuario puede ingresar un idioma y la aplicación muestra todos los libros registrados en ese idioma.

## Cómo Ejecutar el Proyecto

1. Clona o descarga el proyecto desde el repositorio.
2. Abre el proyecto en tu entorno de desarrollo Java (por ejemplo, IntelliJ IDEA).
3. Asegúrate de tener configurada la base de datos correctamente.
4. Ejecuta la clase `Principal` para comenzar a interactuar con la aplicación.

## Ejemplo de Uso

Al ejecutar la aplicación, el menú principal aparecerá en consola:

```
Elija la opción a través de su número
1- Consultar libro por titulo
2- Listar libros registrados
3- Listar autores registrados
4- Buscar autores vivos en un determinado año de la BD
5- Buscar libros registrados en la BD por idioma
0 - Salir
```

El usuario puede seleccionar una opción e ingresar los datos solicitados (por ejemplo, el título de un libro o un año para la búsqueda de autores vivos).

## Contribuciones

Si deseas contribuir a este proyecto, puedes:

- Crear un fork del repositorio.
- Realizar tus cambios.
- Enviar un pull request con tus mejoras.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Para más detalles, consulta el archivo LICENSE.
