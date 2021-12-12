package com.company.Utils;

import com.company.Modelos.Tecnico;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class TecnicoCV implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        Tecnico tecnico = (Tecnico) o;

        writer.addAttribute("Id", String.valueOf( tecnico.getId()));

        writer.startNode("nombre");
        writer.setValue(tecnico.getNombre());
        writer.endNode();

        writer.startNode("especialidad");
        writer.setValue(tecnico.getEspecialidad().toString());
        writer.endNode();

        writer.startNode("ciudad");
        writer.setValue(tecnico.getCiudad());
        writer.endNode();

        writer.startNode("categoria");
        writer.setValue(tecnico.getCategoria());
        writer.endNode();

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Tecnico tecnico = new Tecnico();

        //ID
        tecnico.setId(Integer.parseInt(reader.getAttribute("Id")));

        //nombre
        reader.moveDown();
        tecnico.setNombre(reader.getValue());
        reader.moveUp();

        //especialidad
        reader.moveDown();
        tecnico.setEspecialidad(TechSpecialization.valueOf(reader.getValue()));
        reader.moveUp();

        //ciudad
        reader.moveDown();
        tecnico.setCiudad(reader.getValue());
        reader.moveUp();

        //categoria
        reader.moveDown();
        tecnico.setCategoria(reader.getValue());
        reader.moveUp();



        return tecnico;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Tecnico.class);
    }
}
