package com.company.Controlador.Ficheros;

import com.company.Modelos.Area;

import java.util.*;

public class AreasEmpresa {

    private static final Map<Integer, Area> areaMap= new HashMap<>();

    public AreasEmpresa() { }

    public static void add(Area obj) {
        if (!getMap().containsKey(obj.getId())){
            getMap().put(obj.getId(),obj);
        }
    }

    public static List<Area> getLista() {
        return new ArrayList<>(getMap().values());
    }

    public static Map<Integer,Area> getMap() {return areaMap;}

    public static void visualizarAreas(){
        for (Area a: getLista())
            System.out.println(a);
    }

    /**
     * Proporciona una Key valida para almacenar en el dicccionario (Id)
     * @return int como Key nueva o -1 si error
     */
    public static int getFreeId(){
        int newKey= -1;
        if (!getMap().isEmpty())
            newKey = (Collections.max(getMap().keySet())) +1 ;
        return newKey;
    }
}
