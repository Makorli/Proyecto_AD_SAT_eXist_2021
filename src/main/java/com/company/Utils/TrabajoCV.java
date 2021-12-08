package com.company.Utils;

import com.company.Modelos.Trabajo;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class TrabajoCV implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        Trabajo trabajo = (Trabajo) o;

        //writer.addAttribute("id",trabajo.get);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Trabajo trabajo = new Trabajo();

        return trabajo;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Trabajo.class);
    }
}
