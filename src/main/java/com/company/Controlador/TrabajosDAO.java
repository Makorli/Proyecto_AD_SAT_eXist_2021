package com.company.Controlador;

import com.company.Modelos.Trabajo;
import com.company.Utils.TrabajoCV;
import com.company.Utils.Utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TrabajosDAO {

    private Collection col;
    //Cadena de inicio de localizacion de los objetos XML en Exist
    private final String myRoot = "Trabajos";
    private final String myNode = "Trabajo";
    private final String myIDField = "Id";
    private static final String[] camposUpdate = {"Incidencia", "Tecnico","horas"};

    //CONSTRuCTORES
    public TrabajosDAO() {
        init();
    }

    private void init() {
        col = DBController.getDefaultCollection();
    }

    public TrabajosDAO(Collection col) {
        this.col = col;
    }

    //CREATE
    public boolean insert(Trabajo trabajo) {

        if (trabajo.getId() == 0) {
            trabajo.setId(getNextId());
        }

        XStream xstream = new XStream();
        xstream.processAnnotations(Trabajo.class);
        String objectXML = xstream.toXML(trabajo);

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
                return true;
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
    public Trabajo getObjectById(int id) {
        if (!existId(id)) return null;
        Trabajo trabajo;
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery = String.format("/%s/%s[@Id=%d]",
                        myRoot,
                        myNode,
                        id);
                ResourceSet result = servicio.query(myQuery);
                ResourceIterator i;
                i = result.getIterator();
                col.close();
                //Obtenemos XML
                String objectXML = (String) i.nextResource().getContent();
                //Convertimos de XML a objeto
                XStream xStream = new XStream();
                xStream.alias("Trabajo", Trabajo.class); //Seteamos la etiqueta root del XML
                xStream.addPermission(AnyTypePermission.ANY);       //Permtimos los permisos de desaserializacion
                xStream.registerConverter(new TrabajoCV());     //Asignamos conversor
                trabajo = (Trabajo) xStream.fromXML(objectXML); //Obtenemos objeto
                return trabajo;

            } catch (XMLDBException e) {
                System.out.println("Error al consultar.");
                // e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
        return null;
    }

    public String getXMLById(int id) {
        if (!existId(id)) return null;
        Trabajo trabajo;
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery = String.format("/%s/%s[@Id=%d]",
                        myRoot,
                        myNode,
                        id);
                ResourceSet result = servicio.query(myQuery);
                ResourceIterator i;
                i = result.getIterator();
                col.close();
                //Obtenemos XML
                String objectXML = (String) i.nextResource().getContent();

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

    public boolean existId(int id) {
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery = String.format("/%s/%s//@%s=%d",
                        myRoot, myNode, myIDField, id);
                ResourceSet result = servicio.query(myQuery);
                col.close();
                //Retornamos si existe o no el id
                return Boolean.parseBoolean((String) result.getResource(0L).getContent());
            } catch (Exception e) {
                System.out.println("Error al consultar.");
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
        return false;
    }

    public List<Trabajo> getAllObjects() {
        List<Trabajo> myList = new ArrayList<>();
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery = String.format("for $trabajo in /%s/%s return $trabajo", myRoot, myNode);
                ResourceSet result = servicio.query(myQuery);
                ResourceIterator i = result.getIterator();
                col.close();

                //Preparamos conversion de XML a objetos
                XStream xStream = new XStream();
                xStream.alias("Trabajo", Trabajo.class);
                xStream.addPermission(AnyTypePermission.ANY);       //Permtimos los permisos de desaserializacion
                xStream.registerConverter(new TrabajoCV());     //Asignamos conversor

                while (i.hasMoreResources()) {
                    String objectXML = (String) i.nextResource().getContent();
                    Trabajo trabajo = (Trabajo) xStream.fromXML(objectXML);
                    myList.add(trabajo);
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

    public List<String> getAllXMLObjects() {
        List<String> myList = new ArrayList<>();
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery = String.format("for $trabajo in /%s/%s return $trabajo", myRoot, myNode);
                ResourceSet result = servicio.query(myQuery);
                ResourceIterator i = result.getIterator();
                col.close();

                while (i.hasMoreResources()) {
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
    public boolean update(Trabajo trabajo) {
        int idTrabajo = trabajo.getId();

        if (existId(idTrabajo)) {
            if (col != null) {
                try {
                    //PAsamos el objeto a XML
                    XStream xStream = new XStream();
                    //xStream.registerConverter(new TrabajosCV());
                    xStream.processAnnotations(Trabajo.class);
                    String myXMLObj = xStream.toXML(trabajo);

                    //Modifico el objeto XML por completo, paso de hacerlo campo por campo
                    // La modificacion la hago mediante el borrado y la adicion del nuevo elemento.
                    delete(trabajo);
                    insert(trabajo);

                    System.out.println("Trabajo actualizada.");
                    return true;
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
    public boolean delete(Trabajo trabajo) {
        int idTrabajo = trabajo.getId();

        if (existId(idTrabajo)) {

            if (col != null) {
                try {
                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    String myQuery = String
                            .format("update delete /%s/%s[@Id='%d']",
                                    myRoot,
                                    myNode,
                                    trabajo.getId()
                            );
                    ResourceSet result = servicio.query(myQuery);
                    col.close();
                    return true;
                } catch (Exception e) {
                    System.out.println("Error al borrar.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println("La trabajo NO EXISTE.");
        }
        return false;
    }

    /**
     * REtorna el siguiente ID disponible en la clase.
     *
     * @return siguiente ID disponible en la base de datos para esa clase.
     */
    public int getNextId() {
        //Devuelve siguiente Id lógico defindio en la clase. Caldula el máximo Id y devuelve
        // el siguiente número.
        int myId = 1;
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //max(//TrabajosReportadas/Trabajo//@Id)
                String myQuery = String.format("max(//%s/%s//@%s)",
                        this.myRoot,
                        this.myNode,
                        this.myIDField);
                ResourceSet result = servicio.query(myQuery);
                if (result.getSize() > 0L) {
                    Resource r = result.getResource(0L);
                    myId = Integer.parseInt(r.getContent().toString()) + 1;
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

    public Trabajo seleccionar() {
        boolean todok = false;
        int id = -1;
        Scanner scan = new Scanner(System.in);
        List<Trabajo> trabajoList = getAllObjects();
        for (Trabajo t: trabajoList){
            System.out.println(t);
        }
        do {
            id = Utils.leerdato(id, "Introduce código de Trabajo: ");
            if (existId(id)) todok = true;
            else System.out.println("Id proporcionado no existe");
        } while (!todok);

        return getObjectById(id);
    }

}
