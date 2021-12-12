package com.company.Modelos;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;

import java.io.Serializable;

/**
 * Clase para almacenar las incidencias registradas para su resolución o consulta.
 * Se debe usar siempre el metodo *setatributo* para establecer los valores de los mismos
 * ya que el control de tamaño esta implementado en el propio campo.
 */
@XStreamAlias("Incidencia")
public class Incidencia implements Serializable {

    //ATRIBUTOS
    @XStreamAlias("Id")
    @XStreamAsAttribute()
    private int id;   //Id de la incidencia

    private String descripcion; //Tipo de la incidencia (100 caracteres)

    private int IdArea;        //Area que registra la incidencia (15 caracteres)

    @XStreamAlias("Resuelta")
    @XStreamAsAttribute
    @XStreamConverter(value= BooleanConverter.class, booleans={false}, strings={"SI", "NO"})
    private boolean resuelta;   //Indica si la incidencia está resuelta o no

    private int horas;  //Indicador de horas dedicadas a la incidencia

    private int idTecnicoCierre; //tecnico que solucino la incidencia

    //CONSTRUCTORES
    public Incidencia() {
    }

    public Incidencia(int id, String descripcion, int idArea) {
        this.id = id;
        setDescripcion(descripcion); //Tipo de Incidencia
        setIdArea(idArea);
        this.resuelta = false;
        this.horas=0;
        idTecnicoCierre=0;
    }

    // GETTERS Y SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        //Control de máximo de 100 caracteres
        this.descripcion = descripcion;
    }

    public int getIdArea() {
        return IdArea;
    }

    public void setIdArea(int idArea) {
        //Permitido máximo 15 carácteres
        //this.area = Utils.setStringToFixedSize(area.trim(),15);
        this.IdArea = idArea;
    }


    public boolean isResuelta() {
        return resuelta;
    }

    public void setResuelta(boolean resuelta) {
        this.resuelta = resuelta;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public void addHoras(int horas) { this.horas+=horas;}

    public int getIdTecnicoCierre() {
        return idTecnicoCierre;
    }

    public void setIdTecnicoCierre(int idTecnicoCierre) {
        this.idTecnicoCierre = idTecnicoCierre;
    }

    @Override
    public String toString() {
        return "Incidencia id: " + id +
                ", descripcion: " + descripcion +
                ", area: " + IdArea  +
                ", resuelta: " + resuelta +
                ", Horas acumuladas: " + horas +
                (isResuelta()?(" ,Tecnico Cierre: "+idTecnicoCierre):"");
    }
}
