package com.company.Controlador.Ficheros;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para almacenamiento genérico de las incidencias.
 */
public class TiposDeIncidencias {

    //Lista de Tipos de incidencia el indice de la tabla actua como ID de la misma
    private static final List<String> tiposdeincidencialist = new ArrayList<>();

    public TiposDeIncidencias() { }

    public static void add(String inc) {
        tiposdeincidencialist.add(inc);
    }

    public static void visualizar(){
        for (String s: getLista())
            System.out.println(s);
    }

    public static List<String> getLista() {
        return tiposdeincidencialist;
    }
}
