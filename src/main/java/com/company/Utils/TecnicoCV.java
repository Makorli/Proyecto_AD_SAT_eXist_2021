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

        writer.addAttribute("id", String.valueOf( tecnico.getId()));
        writer.addAttribute("especialidad", tecnico.getEspecialidad().toString());
        //writer.setValue(tecnico.getNombre());

        writer.startNode("nombre");
        writer.setValue(tecnico.getNombre());
        writer.endNode();

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Tecnico tecnico = new Tecnico();

        tecnico.setId(Integer.parseInt(reader.getAttribute("id")));
        tecnico.setEspecialidad(TechSpecialization.valueOf(reader.getAttribute("especialidad")));
        //tecnico.setNombre(reader.getValue());

        reader.moveDown();
        tecnico.setNombre(reader.getValue());
        reader.moveUp();

        return tecnico;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Tecnico.class);
    }
}
