package com.company.Utils;

import com.company.Modelos.Area;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class AreasCV implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        Area area = (Area) o;

        //Atributos
        writer.addAttribute("Id", String.valueOf(area.getId()));

        //nombre
        writer.startNode("Nombre");
        writer.setValue(area.getNombreArea());
        writer.endNode();

        //Numero Personas
        writer.startNode("numeroPersonas");
        writer.setValue(String.valueOf(area.getNumeroPersonas()));
        writer.endNode();

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Area area = new Area();

        //Atributos
        area.setId(Integer.parseInt(reader.getAttribute("Id")));

        //nombre
        reader.moveDown();
        area.setNombreArea(String.valueOf(reader.getValue()));
        reader.moveUp();

        //numero de personas
        reader.moveDown();
        area.setNumeroPersonas(Integer.parseInt(reader.getValue()));
        reader.moveUp();

        return area;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Area.class);
    }

}
