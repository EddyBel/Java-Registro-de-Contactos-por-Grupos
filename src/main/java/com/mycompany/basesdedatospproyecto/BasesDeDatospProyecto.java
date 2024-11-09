/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.basesdedatospproyecto;

/**
 * Clase principal del proyecto "Bases de Datos PProyecto".
 *
 * Esta clase contiene el método principal `main` que es el punto de entrada de
 * la aplicación. Al ejecutarse, inicializa y muestra la interfaz gráfica del
 * proyecto con el título "Proyecto Bases de Datos". La interfaz es gestionada
 * por la clase `Interfaz`, que representa la ventana principal de la
 * aplicación.
 *
 * @author ben_9
 */
public class BasesDeDatospProyecto {

    /**
     * Método principal de la aplicación.
     *
     * Este método es el punto de entrada de la aplicación. Inicializa una
     * instancia de la clase `Interfaz`, le asigna un título a la ventana y la
     * hace visible para el usuario.
     *
     * @param args Argumentos de línea de comandos (no utilizados en este caso).
     */
    public static void main(String[] args) {
        // Crear una nueva instancia de la interfaz
        Interfaz interfaz = new Interfaz();
        // Establecer el título de la ventana
        interfaz.setTitle("Proyecto Bases de Datos");
        // Hacer visible la interfaz
        interfaz.setVisible(true);
    }
}
