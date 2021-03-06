package com.company.Modelos;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

@XStreamAlias("Area")
public class Area implements Serializable {

    @XStreamAlias("Id")
    @XStreamAsAttribute()
    private int id;
    @XStreamAlias("Nombre")
    private String nombreArea;
    private int numeroPersonas;

    //CONSTRUCTORES
    public Area() {
    }

    public Area(int id, String nombreArea) {
        this.id = id;
        this.nombreArea = nombreArea;
        this.numeroPersonas = 0;
    }

    public Area(int id, String nombreArea, int numeroPersonas) {
        this.id = id;
        this.nombreArea = nombreArea;
        this.numeroPersonas = numeroPersonas;
    }

    //GETTERS Y SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {

        //this.nombreArea = Utils.setStringToFixedSize(nombreArea,15);
        this.nombreArea = nombreArea;
    }

    public int getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(int numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }

    @Override
    public String toString() {
        return "Area Id: " + id +
                ", Nombre: " + nombreArea +
                ", Personas: " + numeroPersonas;
    }
}
