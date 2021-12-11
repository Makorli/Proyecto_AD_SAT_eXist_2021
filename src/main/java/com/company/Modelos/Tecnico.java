package com.company.Modelos;

import com.company.Utils.TechSpecialization;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * Clase Tecnico. Implemneta de serializable para su uso con flujos de datos.
 * Se debe usar siempre el metodo *setatributo* para establecer los valores de los mismos
 * ya que el control de tamaño esta implementado en el propio campo.
 */

@XStreamAlias("Tecnico")
public class Tecnico implements Serializable {

    //ATRIBUTOS

    @XStreamAlias("Id")
    @XStreamAsAttribute()
    private int id;
    private String nombre;
    private TechSpecialization especialidad;
    private String ciudad;
    private String categoria;


    //CONSTRUCTORES

    public Tecnico() { }

    public Tecnico(int id, String nombre, TechSpecialization especialidad, String ciudad, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        setCiudad(ciudad);
        setCategoria(categoria);
    }

//GETTERSY SETTERS

    public int getId() {
        return id;
    }

    public void setId(int idTecnico) {
        this.id = idTecnico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) { this.nombre =nombre; }

    public TechSpecialization getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(TechSpecialization especialidad) {
        this.especialidad = especialidad;
    }

    public String getCiudad() { return ciudad; }

    public void setCiudad(String ciudad) { this.ciudad = ciudad.toUpperCase().trim(); }

    public String getCategoria() { return categoria; }

    public void setCategoria(String categoria) { this.categoria = categoria.toLowerCase().trim(); }

    @Override
    public String toString() {
        return "Tecnico Id: " + id +
                ", Nombre: " + nombre +
                ", Especialidad: " + (getEspecialidad()==null?"":getEspecialidad().toString()) +
                ", Ciudad: " + ciudad +
                ", Categoria: " + categoria ;
    }
}
