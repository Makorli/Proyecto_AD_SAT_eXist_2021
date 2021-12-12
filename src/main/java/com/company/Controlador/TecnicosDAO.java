package com.company.Controlador;


import com.company.Modelos.Area;
import com.company.Modelos.Tecnico;
import com.company.Utils.TecnicoCV;
import com.company.Utils.Utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TecnicosDAO {

    private Collection col;
    //Cadena de inicio de localizacion de los objetos XML en Exist
    private final String myRoot = "DepartamentoTecnico";
    private final String myNode = "Tecnico";
    private final String myIDField = "Id";
    private static final String[] camposUpdate = {"nombre", "especialidad","ciudad","catgoria"};

    //CONSTRuCTORES
    public TecnicosDAO() {
        init();
    }

    private void init() {
        col = DBController.getDefaultCollection();
    }

    public TecnicosDAO(Collection col) {
        this.col = col;
    }

    //CREATE
    public boolean insert(Tecnico tecnico) {

        if (tecnico.getId() == 0) {
            tecnico.setId(getNextId());
        }

        XStream xstream = new XStream();
        xstream.processAnnotations(Tecnico.class);
        String objectXML = xstream.toXML(tecnico);

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
    public Tecnico getObjectById(int id) {
        if (!existId(id)) return null;
        Tecnico tecnico;
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
                xStream.alias("Tecnico", Tecnico.class); //Seteamos la etiqueta root del XML
                xStream.addPermission(AnyTypePermission.ANY);       //Permtimos los permisos de desaserializacion
                xStream.registerConverter(new TecnicoCV());     //Asignamos conversor
                tecnico = (Tecnico) xStream.fromXML(objectXML); //Obtenemos objeto
                return tecnico;

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
        Tecnico tecnico;
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

    public List<Tecnico> getAllObjects() {
        List<Tecnico> myList = new ArrayList<>();
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery = String.format("for $tecnico in /%s/%s return $tecnico", myRoot, myNode);
                ResourceSet result = servicio.query(myQuery);
                ResourceIterator i = result.getIterator();
                col.close();

                //Preparamos conversion de XML a objetos
                XStream xStream = new XStream();
                xStream.alias("Tecnico", Tecnico.class);
                xStream.addPermission(AnyTypePermission.ANY);       //Permtimos los permisos de desaserializacion
                xStream.registerConverter(new TecnicoCV());     //Asignamos conversor

                while (i.hasMoreResources()) {
                    String objectXML = (String) i.nextResource().getContent();
                    Tecnico tecnico = (Tecnico) xStream.fromXML(objectXML);
                    myList.add(tecnico);
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
                String myQuery = String.format("for $tecnico in /%s/%s return $tecnico", myRoot, myNode);
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
    public boolean update(Tecnico tecnico) {
        int idTecnico = tecnico.getId();

        if (existId(idTecnico)) {
            if (col != null) {
                try {
                    //PAsamos el objeto a XML
                    XStream xStream = new XStream();
                    //xStream.registerConverter(new TecnicosCV());
                    xStream.processAnnotations(Tecnico.class);
                    String myXMLObj = xStream.toXML(tecnico);

                    //Modifico el objeto XML por completo, paso de hacerlo campo por campo
                    // La modificacion la hago mediante el borrado y la adicion del nuevo elemento.
                    delete(tecnico);
                    insert(tecnico);

                    System.out.println("Tecnico actualizada.");
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
    public boolean delete(Tecnico tecnico) {
        int idTecnico = tecnico.getId();

        if (existId(idTecnico)) {

            if (col != null) {
                try {
                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    String myQuery = String
                            .format("update delete /%s/%s[@Id='%d']",
                                    myRoot,
                                    myNode,
                                    tecnico.getId()
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
            System.out.println("La tecnico NO EXISTE.");
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
                //max(//TecnicosReportadas/Tecnico//@Id)
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

    public Tecnico seleccionar() {
        boolean todok = false;
        int id = -1;
        Scanner scan = new Scanner(System.in);
        List<Tecnico> tecnicoList = getAllObjects();
        for (Tecnico t: tecnicoList){
            System.out.println(t);
        }
        do {
            id = Utils.leerdato(id, "Introduce código de Tecnico");
            if (existId(id)) todok = true;
            else System.out.println("Id proporcionado no existe");
        } while (!todok);

        return getObjectById(id);
    }
}
