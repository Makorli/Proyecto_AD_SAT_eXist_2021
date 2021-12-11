package com.company.Controlador;

import com.company.Modelos.Area;
import com.company.Modelos.Incidencia;
import com.company.Utils.AreasCV;
import com.company.Utils.Utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AreasDAO {

    private Collection col;
    //Cadena de incio de localizacion de los objetos XML en Exist
    private String myRoot ="AreasEmpresa";
    private String myNode ="Area";
    private String myIDField ="Id";
    private static String[] camposUpdate = {"Nombre", "Numero Personas"};

    //CONSTRuCTORES
    public AreasDAO() {
        init();
    }
    private void init(){
        col= DBController.getDefaultCollection();
    }

    public AreasDAO(Collection col) {
        this.col = col;
    }


    //CREATE
    public boolean insert(Area area){
        XStream xstream = new XStream();
        xstream.processAnnotations(Area.class);
        String objectXML = xstream.toXML(area);

        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para insertar --> update insert ... into
                String myQuery = String.format("update insert %s into /%s",
                        objectXML,
                        myRoot);
                ResourceSet result = servicio.query(myQuery);
                col.close();
                System.out.println("Insertado.");
            } catch (Exception e) {
                System.out.println("Error al Insertar.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }

        return false;
    }

    //RETRIEVE
    public Area getObjectById(int id){
        if (!existId(id)) return null;
        Area area;
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery= String.format("/%s/%s[@Id=%d]",
                        myRoot,
                        myNode,
                        id);
                ResourceSet result = servicio.query(myQuery);
                ResourceIterator i;
                i = result.getIterator();
                col.close();
                //Obtenemos XML
                String objectXML= (String) i.nextResource().getContent();
                //Convertimos de XML a objeto
                XStream xStream = new XStream();
                xStream.alias("Area",Area.class); //Seteamos la etiqueta root del XML
                xStream.addPermission(AnyTypePermission.ANY);       //Permtimos los permisos de desaserializacion
                xStream.registerConverter(new AreasCV());     //Asignamos conversor
                area = (Area) xStream.fromXML(objectXML); //Obtenemos objeto
                return area;

            } catch (XMLDBException e) {
                System.out.println("Error al consultar.");
                // e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
        return null;
    }

    public String getXMLById(int id){
        if (!existId(id)) return null;
        Area area;
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery= String.format("/%s/%s[@Id=%d]",
                        myRoot,
                        myNode,
                        id);
                ResourceSet result = servicio.query(myQuery);
                ResourceIterator i;
                i = result.getIterator();
                col.close();
                //Obtenemos XML
                String objectXML= (String) i.nextResource().getContent();

                return objectXML;

            } catch (XMLDBException e) {
                System.out.println("Error al consultar.");
                // e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
        return null;
    }

    public boolean existId(int id){
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery= String.format("/%s/%s//@%s=%d",
                        myRoot, myNode,myIDField,id);
                ResourceSet result = servicio.query(myQuery);
                col.close();
                //Retornamos si existe o no el id
                return Boolean.parseBoolean((String)result.getResource(0L).getContent());
            } catch (Exception e) {
                System.out.println("Error al consultar.");
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
        return false;
    }

    public List<Area> getAllObjects(){
        List<Area> myList = new ArrayList<>();
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery= String.format("for $area in /%s/%s return $area",myRoot,myNode);
                ResourceSet result = servicio.query(myQuery);
                ResourceIterator i = result.getIterator();
                col.close();

                //Preparamos conversion de XML a objetos
                XStream xStream = new XStream();
                xStream.alias("Area",Area.class);
                xStream.addPermission(AnyTypePermission.ANY);       //Permtimos los permisos de desaserializacion
                xStream.registerConverter(new AreasCV());     //Asignamos conversor

                while(i.hasMoreResources()){
                    String objectXML = (String) i.nextResource().getContent();
                    Area area = (Area) xStream.fromXML(objectXML);
                    myList.add(area);
                }

                return myList;

            } catch (XMLDBException e) {
                System.out.println("Error al consultar.");
                // e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
        return myList;
    }

    public List<String> getAllXMLObjects(){
        List<String> myList = new ArrayList<>();
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery= String.format("for $area in /%s/%s return $area",myRoot,myNode);
                ResourceSet result = servicio.query(myQuery);
                ResourceIterator i = result.getIterator();
                col.close();

                while(i.hasMoreResources()){
                    String objectXML = (String) i.nextResource().getContent();
                    myList.add(objectXML);
                }

                return myList;

            } catch (XMLDBException e) {
                System.out.println("Error al consultar.");
                // e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
        return myList;
    }

    //UPDATE
    public boolean update(Area area){
        int idArea= area.getId();

        if (existId(idArea)) {
            if (col != null) {
                try {
                    //PAsamos el objeto a XML
                    XStream xStream= new XStream();
                    //xStream.registerConverter(new AreasCV());
                    xStream.processAnnotations(Area.class);
                    String myXMLObj = xStream.toXML(area);

                    //Modifico el objeto XML por completo, paso de hacerlo campo por campo
                    // La modificacion la hago mediante el borrado y la adicion del nuevo elemento.
                    delete(area);
                    insert(area);

                    System.out.println("Area actualizada.");
                } catch (Exception e) {
                    System.out.println("Error al actualizar.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println("La indicendia proporcionada NO EXISTE.");
        }
        return false;
    }

    //DELETE
    public boolean delete(Area area){
        int idArea= area.getId();

        if (existId(idArea)) {

            if (col != null) {
                try {
                    System.out.printf("Borro la area: %s\n", area.getId());
                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    String myQuery= String
                            .format("update delete /%s/%s[@Id='%d']",
                                    myRoot,
                                    myNode,
                                    area.getId()
                            );
                    ResourceSet result = servicio.query(myQuery);
                    col.close();
                    System.out.println("Area borrada.");
                    return true;
                } catch (Exception e) {
                    System.out.println("Error al borrar.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println("La area NO EXISTE.");
        }
        return false;
    }


    /**
     * REtorna el siguiente ID disponible en la clase.
     * @return siguiente ID disponible en la base de datos para esa clase.
     */
    private int getNextId() {
        //Devuelve siguiente Id lógico defindio en la clase. Caldula el máximo Id y devuelve
        // el siguiente número.
        int myId = 1;
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                String myQuery = String.format("max(//%s/%s//@%s)",
                        this.myRoot,
                        this.myNode,
                        this.myIDField);
                ResourceSet result = servicio.query(myQuery);
                if (result.getSize()>0L) {
                    Resource r = result.getResource(0L);
                    myId = Integer.parseInt(r.getContent().toString())+1;
                }
            } catch (Exception e) {
                System.out.println("Error al obtener IDs.");
                // e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
        return myId;
    }

    public Area seleccionar() {
        boolean todok = false;
        int id = -1;
        List<Area> areaList = getAllObjects();
        for (Area a: areaList){
            System.out.println(a);
        }
        do {
            id = Utils.leerdato(id, "Introduce código de Area: ");
            if (existId(id)) todok = true;
            else{
                System.out.println("\nEse Area No existe, vuelve a intentarlo.\n");
            }
        } while (!todok);

        return getObjectById(id);
    }

    /**
     * Procedmiento de lectura y creación de una incidencia.
     *
     * @return
     */
    public Area crear() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int opcion = -1;
        boolean todok = false;

        try {
            //Leemos nombre de nuevo Area y verificamos que no exista
            String nombre = "";

            do {
                todok =false;
                System.out.println("Nombre del Area: ");
                nombre = br.readLine().trim();
                if (existName(nombre)){
                    System.out.println("El nombre ya existe");
                } else todok=true;
            } while (nombre.equals("") && !todok);

            //Solicitamos Numero de Personas
            int npersonas = -1;
            do {
                npersonas = Utils.leerdato(npersonas,"Numero de personas en el Area");
            } while (npersonas<0);

            //Creamos la nueva Area
            Area area = new Area();
            area.setNombreArea(nombre);
            area.setNumeroPersonas(npersonas);

            return area;

        } catch (IOException e) {
            System.out.println("Error en la lectura de datos");
        }

        return null;
    }

    public void modificar() {
        //Seleccionamos area a modificar
        Area area = seleccionar();
        //Seleccionamos el campo a modificar
        int idx = -1;
        do {
            for (int i = 0; i < camposUpdate.length; i++) {
                System.out.printf("%d.- %s\n", i + 1, camposUpdate[i]);
            }
            idx = Utils.leerdato(idx, "Introduce selector de campo");
            idx--;
        } while ((idx < 0) || idx >= camposUpdate.length);

        switch(idx){
            case 0:
                boolean todok=false;
                String nombre="";
                do {
                    todok =false;

                    System.out.println("Nombre del Area: ");
                    nombre = Utils.leerdato(nombre,"Nombre del Area");
                    nombre= nombre.trim();
                    if (existName(nombre)){
                        System.out.println("El nombre ya existe");
                    } else todok=true;
                } while (nombre.equals("") && !todok);
                area.setNombreArea(nombre);
                break;
            case 1:
                //Solicitamos Numero de Personas
                int npersonas = -1;
                do {
                    npersonas = Utils.leerdato(npersonas,"Numero de personas en el Area");
                } while (npersonas<0);
                area.setNumeroPersonas(npersonas);

                break;
            default:{}
        }

        //Modificamos el area
        if (update(area))
            System.out.printf("Modificacion correcta.\n%s\n",area);
        else System.out.println("...parece que algo ha ido mal");

    }

    public boolean existName(String name){
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                //TODO verificacion de texto NO case Sensitive
                String myQuery= String.format("/%s/%s//%s=%s",
                        myRoot, myNode,"Nombre",name);
                ResourceSet result = servicio.query(myQuery);
                col.close();
                //Retornamos si existe o no el id
                return Boolean.parseBoolean((String)result.getResource(0L).getContent());
            } catch (Exception e) {
                System.out.println("Error al consultar.");
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
        return false;
    }
}
