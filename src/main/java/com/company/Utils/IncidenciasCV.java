package com.company.Utils;

import com.company.Modelos.Incidencia;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class IncidenciasCV implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        Incidencia incidencia = (Incidencia) o;

        writer.addAttribute("id", String.valueOf(incidencia.getId()));
        writer.addAttribute("area", String.valueOf(incidencia.getIdArea()));
        writer.addAttribute("resuelta", String.valueOf(incidencia.isResuelta()));

        writer.startNode("descripcion");
        writer.setValue(incidencia.getDescripcion());
        writer.endNode();

        writer.startNode("horas");
        writer.setValue(String.valueOf(incidencia.getHoras()));
        writer.endNode();

        writer.startNode("closer");
        writer.setValue(String.valueOf(incidencia.getIdTecnicoCierre()));
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Incidencia incidencia = new Incidencia();

        incidencia.setId(Integer.parseInt(reader.getAttribute("id")));
        incidencia.setIdArea(Integer.parseInt(reader.getAttribute("area")));
        incidencia.setResuelta(Boolean.parseBoolean(reader.getAttribute("resuelta")));
        //tecnico.setNombre(reader.getValue());

        reader.moveDown();
        incidencia.setDescripcion(reader.getValue());
        reader.moveUp();

        reader.moveDown();
        incidencia.setHoras(Integer.parseInt(reader.getValue()));
        reader.moveUp();

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
