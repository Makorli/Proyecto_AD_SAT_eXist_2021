package com.company.Controlador.Ficheros;

import com.company.Modelos.Trabajo;

import java.util.ArrayList;
import java.util.List;

public class TrabajosRealizados {

    private static final List<Trabajo> trabajoList = new ArrayList<>();

    public TrabajosRealizados() { }

    public static void add(Trabajo ded) {
        trabajoList.add(ded);
    }

    public static List<Trabajo> getLista() {
        return trabajoList;
    }
}