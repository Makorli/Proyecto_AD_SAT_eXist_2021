package com.company.Utils;

import com.company.Modelos.Incidencia;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class IncidenciasCV implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        Incidencia incidencia = (Incidencia) o;

        //Atributos
        writer.addAttribute("Id", String.valueOf(incidencia.getId()));
        writer.addAttribute("Resuelta", incidencia.isResuelta()?"SI":"NO");

        //descripcion
        writer.startNode("descripcion");
        writer.setValue(incidencia.getDescripcion());
        writer.endNode();

        //IdArea
        writer.startNode("IdArea");
        writer.setValue(String.valueOf(incidencia.getIdArea()));
        writer.endNode();

        //horas
        writer.startNode("horas");
        writer.setValue(String.valueOf(incidencia.getHoras()));
        writer.endNode();

        //idTecnicoCierre
        writer.startNode("idTecnicoCierre");
        writer.setValue(String.valueOf(incidencia.getIdTecnicoCierre()));
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Incidencia incidencia = new Incidencia();

        //Atributos
        incidencia.setId(Integer.parseInt(reader.getAttribute("Id")));
        String tmp = reader.getAttribute("Resuelta");
        incidencia.setResuelta(tmp.equalsIgnoreCase("SI"));

        //descripcion
        reader.moveDown();
        incidencia.setDescripcion(String.valueOf(reader.getValue()));
        reader.moveUp();

        //IdArea
        reader.moveDown();
        incidencia.setIdArea(Integer.parseInt(reader.getValue()));
        reader.moveUp();

        //Horas
        reader.moveDown();
        incidencia.setHoras(Integer.parseInt(reader.getValue()));
        reader.moveUp();

        //idTecnicoCierre
        reader.moveDown();
        incidencia.setIdTecnicoCierre(Integer.parseInt(reader.getValue()));
        reader.moveUp();

        return incidencia;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Incidencia.class);
    }


}
