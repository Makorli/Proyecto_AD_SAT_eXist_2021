package com.company.Controlador;

import com.company.Modelos.Tecnico;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.*;

@XStreamAlias("Departamento Tecnico")
public class DepartamentoTecnico {

    @XStreamImplicit(itemFieldName="Tecnico")
    private static Map<Integer, Tecnico> tecnicoMap = new HashMap<>();

    public DepartamentoTecnico() {
    }

    public static void add(Tecnico obj) {
        if (!getMap().containsKey(obj.getId())){
            getMap().put(obj.getId(),obj);
        }
    }

    /**
     * Actualiza y devuelve la lista asociada al Map con los valores asociados
     * @return lista
     */
    public static List<Tecnico> getLista() {
        return new ArrayList<>(getMap().values());
    }

    public static Map<Integer, Tecnico> getMap(){ return tecnicoMap; }

    public static void VisualizarTecnicos(){
        for (Tecnico t: getLista()){
            System.out.println("t");
        }
    }

    /**
     * Proporciona una Key valida para almacenar en el dicccionario (Id)
     * @return int como Key nueva o -1 si error
     */
    public static int getFreeId(){
        int newKey= 0;
        if (!getMap().isEmpty())
            newKey = (Collections.max(getMap().keySet())) +1 ;
        return newKey;
    }

}
