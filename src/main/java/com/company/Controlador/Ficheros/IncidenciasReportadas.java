package com.company.Controlador.Ficheros;

import com.company.Modelos.Incidencia;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.*;

@XStreamAlias("Incidencias")
public class IncidenciasReportadas {

    @XStreamImplicit(itemFieldName = "TeCnicooo")
    private static final Map<Integer, Incidencia> incidenciaMap = new HashMap<>();
    private static final List<Incidencia> incidenciasCerradasMap = new ArrayList<>();
    private static final List<Incidencia> incidenciasNoCerradasMap = new ArrayList<>();

    public IncidenciasReportadas() {
    }

    public static void add(Incidencia obj) {

        if (!getMap().containsKey(obj.getId())) {
            getMap().put(obj.getId(), obj);
        }

        if (obj.isResuelta())
            getIncidenciasCerradasList().add(obj);
        else
            getIncidenciasNoCerradasList().add(obj);
    }

    public static List<Incidencia> getLista() {
        return new ArrayList<Incidencia>(getMap().values());

    }

    public static Map<Integer, Incidencia> getMap() {
        return incidenciaMap;
    }

    public static List<Incidencia> getIncidenciasCerradasList() {
        return incidenciasCerradasMap;
    }

    public static List<Incidencia> getIncidenciasNoCerradasList() {
        return incidenciasNoCerradasMap;
    }

    public static void visualizarIncidencias(){
        for (Incidencia i : getLista())
            System.out.println(i);
    }

    public static void visualizarIncidenciasByState (boolean cerradas){
        List<Incidencia> incidenciaList;
        if (cerradas) incidenciaList = getIncidenciasCerradasList();
        else incidenciaList = getIncidenciasNoCerradasList();

        for (Incidencia i : incidenciaList){
            System.out.println(i);
        }
    }

    /**
     * Proporciona una Key valida para almacenar en el dicccionario (Id)
     *
     * @return int como Key nueva o -1 si error
     */
    public static int getFreeId() {
        int newKey = 1;
        if (!getMap().isEmpty())
            newKey = (Collections.max(getMap().keySet())) + 1;
        return newKey;
    }
}
