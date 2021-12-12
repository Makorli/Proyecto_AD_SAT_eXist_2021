package com.company.Controlador;

import com.company.Modelos.Area;
import com.company.Modelos.Incidencia;
import com.company.Modelos.Tecnico;
import com.company.Modelos.Trabajo;
import com.company.Utils.IncidenciasCV;
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
import java.util.Scanner;

public class IncidenciasDAO {

    private Collection col;
    //Cadena de inicio de localizacion de los objetos XML en Exist
    private final String myRoot = "IncidenciasReportadas";
    private final String myNode = "Incidencia";
    private final String myIDField = "Id";
    private static final String[] camposUpdate = {"Area", "Descripcion"};

    //CONSTRuCTORES
    public IncidenciasDAO() {
        init();
    }

    private void init() {
        col = DBController.getDefaultCollection();
    }

    public IncidenciasDAO(Collection col) {
        this.col = col;
    }


    //CREATE
    public boolean insert(Incidencia incidencia) {

        if (incidencia.getId() == 0) {
            incidencia.setId(getNextId());
        }

        XStream xstream = new XStream();
        xstream.processAnnotations(Incidencia.class);
        String objectXML = xstream.toXML(incidencia);

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
    public Incidencia getObjectById(int id) {
        if (!existId(id)) return null;
        Incidencia incidencia;
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
                xStream.alias("Incidencia", Incidencia.class); //Seteamos la etiqueta root del XML
                xStream.addPermission(AnyTypePermission.ANY);       //Permtimos los permisos de desaserializacion
                xStream.registerConverter(new IncidenciasCV());     //Asignamos conversor
                incidencia = (Incidencia) xStream.fromXML(objectXML); //Obtenemos objeto
                return incidencia;

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
        Incidencia incidencia;
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

    public List<Incidencia> getAllObjects() {
        List<Incidencia> myList = new ArrayList<>();
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery = String.format("for $incidencia in /%s/%s return $incidencia", myRoot, myNode);
                ResourceSet result = servicio.query(myQuery);
                ResourceIterator i = result.getIterator();
                col.close();

                //Preparamos conversion de XML a objetos
                XStream xStream = new XStream();
                xStream.alias("Incidencia", Incidencia.class);
                xStream.addPermission(AnyTypePermission.ANY);       //Permtimos los permisos de desaserializacion
                xStream.registerConverter(new IncidenciasCV());     //Asignamos conversor

                while (i.hasMoreResources()) {
                    String objectXML = (String) i.nextResource().getContent();
                    Incidencia incidencia = (Incidencia) xStream.fromXML(objectXML);
                    myList.add(incidencia);
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
                String myQuery = String.format("for $incidencia in /%s/%s return $incidencia", myRoot, myNode);
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
    public boolean update(Incidencia incidencia) {
        int idIncidencia = incidencia.getId();

        if (existId(idIncidencia)) {
            if (col != null) {
                try {
                    //PAsamos el objeto a XML
                    XStream xStream = new XStream();
                    //xStream.registerConverter(new IncidenciasCV());
                    xStream.processAnnotations(Incidencia.class);
                    String myXMLObj = xStream.toXML(incidencia);

                    //Modifico el objeto XML por completo, paso de hacerlo campo por campo
                    // La modificacion la hago mediante el borrado y la adicion del nuevo elemento.
                    delete(incidencia);
                    insert(incidencia);

                    System.out.println("Incidencia actualizada.");
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

    public boolean closeIncidencia(int id, Tecnico tecnicoCierre) {
        if (tecnicoCierre == null) tecnicoCierre = new Tecnico();
        init();
        if (existId(id)) {
            if (isOpen(id)) {
                if (col != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String lectura = "";

                    Tecnico t = tecnicoCierre;
                    try {

                        if (!new TecnicosDAO().existId(tecnicoCierre.getId())) {
                            //Especificacion del tecnico de cierre.
                            lectura = Utils.leerdato(lectura, "Especificar técnico de cierre? (S/N)");
                            if (lectura.equalsIgnoreCase("SI") || lectura.equalsIgnoreCase("S")) {
                                t = new TecnicosDAO().seleccionar();
                            }
                        } else t = tecnicoCierre;

                        //Preparamos el servicio XML
                        XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

                        //Modificamos el valor del campo Tecnico de Cierre en el XML
                        String myQuery = String
                                .format("update value /%s/%s[@Id=%d]/idTecnicoCierre with data (%d)",
                                        myRoot,
                                        myNode,
                                        id,
                                        t.getId()
                                );
                        ResourceSet result = servicio.query(myQuery);

                        //Modificamos el atributo resuelta de la incidencia en la base de datos
                        myQuery = String
                                .format("update value /%s/%s[@Id=%d]/@Resuelta[1] with data (%s)",
                                        myRoot,
                                        myNode,
                                        id,
                                        "\"SI\""
                                );
                        result = servicio.query(myQuery);

                        col.close();

                        System.out.println("Incidencia cerrada correctamente.");
                        return true;
                    } catch (Exception e) {
                        System.out.println("Error al actualizar.");
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Error en la conexión. Comprueba datos.");
                }
            } else {
                System.out.println("No hay cambios.La incidencia seleccionada ya está cerrada.");
            }
        } else {
            System.out.println("La indicendia proporcionada NO EXISTE.");
        }
        return false;
    }

    /**
     * Imputa horas a una incidencia. Para ello , selecciona tecnico y crea un trabajo
     * que inserta en la base de datos. Posteriormente suma las horas a la incidencia.
     *
     * @param id Id de la incidencia a la que se imputarán horas.
     * @return true o false si la operación se ha efectuado correctamente.
     */
    public boolean imputarHoras(int id) {

        if (existId(id)) {
            if (isOpen(id)) {
                init();
                if (col != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    int horas = 0;
                    String lectura = "";
                    boolean resuelta = false;
                    Tecnico t = null;
                    TrabajosDAO ctTrabajo = new TrabajosDAO();
                    try {
                        //Especificacion del tecnico.
                        System.out.println("\nEspecifica el tecnico que ha trabajado");
                        t = new TecnicosDAO().seleccionar();

                        //Lectura de numero de horas trabajadas
                        do {
                            horas = Utils.leerdato(horas, "Horas trabajadas?");
                            if (horas <= 0) System.out.println(" El mínimo permitido es 1 hora");
                        } while (horas <= 0);

                        //Preguntamos si la incidencia se ha resuelto correctamente
                        lectura = Utils.leerdato(lectura, "Se ha resuelto la incidencia? (S/N)");
                        resuelta = lectura.equalsIgnoreCase("SI") || lectura.equalsIgnoreCase("S");

                        //Preparamos el servicio XML
                        XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");

                        //Creamos un nuevo trabajo en la base de datos y lo mostramos en pantalla.
                        Trabajo trabajo = new Trabajo();
                        trabajo.setHoras(horas);
                        trabajo.setIdIncidencia(id);
                        trabajo.setIdTecnico(t.getId());
                        trabajo.setId(ctTrabajo.getNextId());

                        if (ctTrabajo.insert(trabajo)) {

                            //Sumamos las horas del trabajo a las horas actuales de la incidencia
                            //Leemos las horas actuales de la base de datos y actualizamos el
                            //campo con el nuevo valor.

                            //calculo de horas
                            String sdaf = "/IncidenciasReportadas/Incidencia[@Id=1]/horas/text()";
                            String myQuery = String
                                    .format("/%s/%s[@Id=%d]/horas/text()",
                                            myRoot,
                                            myNode,
                                            id
                                    );
                            ResourceSet result = servicio.query(myQuery);
                            horas = Integer.parseInt(String.valueOf(result.getResource(0L).getContent()));
                            horas += trabajo.getHoras();

                            //Actualizacion de horas en incidencia
                            myQuery = String
                                    .format("update value /%s/%s[@Id=%d]/horas with data (%d)",
                                            myRoot,
                                            myNode,
                                            id,
                                            horas
                                    );
                            result = servicio.query(myQuery);

                            col.close();
                            System.out.println("Trabajo añadido con exito a la incidencia\n");
                            System.out.println(trabajo);
                            //Cerramos la incidencia si ha sido resuelta
                            if (resuelta) closeIncidencia(id,t);
                            System.out.println(getObjectById(id));
                            System.out.println("Imputacion de horas correcta.");
                            return true;

                        } else {
                            System.out.println("Algo ha fallado, se anula la operación");
                            return false;
                        }
                    } catch (Exception e) {
                        System.out.println("Error al actualizar.");
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Error en la conexión. Comprueba datos.");
                }
            } else {
                System.out.println("No se pueden imputar horas a una incidencia ya cerrada.");
            }
        } else {
            System.out.println("La indicendia proporcionada NO EXISTE.");
        }
        return false;
    }

    //DELETE
    public boolean delete(Incidencia incidencia) {
        int idIncidencia = incidencia.getId();

        if (existId(idIncidencia)) {

            if (col != null) {
                try {
                    //TODO Borrar los trabajos asociados a la incidencia.
                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    String myQuery = String
                            .format("update delete /%s/%s[@Id='%d']",
                                    myRoot,
                                    myNode,
                                    incidencia.getId()
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
            System.out.println("La incidencia NO EXISTE.");
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
                //max(//IncidenciasReportadas/Incidencia//@Id)
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

    public Incidencia seleccionar() {
        boolean todok = false;
        int id = -1;
        Scanner scan = new Scanner(System.in);
        do {
            id = Utils.leerdato(id, "Introduce código de Incidencia");
            if (existId(id)) todok = true;
            else System.out.println("Id proporcionado no existe");
        } while (!todok);

        return getObjectById(id);
    }

    /**
     * Procedmiento de lectura y creación de una incidencia.
     *
     * @return
     */
    public Incidencia crear() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int opcion = -1;
        boolean todok = false;

        try {
            //Solicitamos Area
            List<Area> areaList = new AreasDAO().getAllObjects();
            for (int i = 0; i < areaList.size(); i++) {
                System.out.printf("%d.- %S\n", i + 1, areaList.get(i).getNombreArea());
            }
            do {
                opcion = Utils.leerdato(opcion, "Introduce selector Area: ");
                opcion--;
            } while ((opcion < 0) || opcion >= areaList.size());
            Area area = areaList.get(opcion);

            //Solicitamos Descripcion
            String descripcion = "";
            do {
                System.out.println("Descipción de la incidencia: ");
                descripcion = br.readLine().trim();
            } while (descripcion.equals(""));

            //Creamos la nueva incidencia
            Incidencia incidencia = new Incidencia();
            incidencia.setIdArea(area.getId());
            incidencia.setDescripcion(descripcion);

            return incidencia;

        } catch (IOException e) {
            System.out.println("Error en la lectura de datos");
        }

        return null;
    }

    public void modificar() {
        //Seleccionamos incidencia modificar
        Incidencia incidencia = seleccionar();
        System.out.printf("Seleccionada incidecnia\n", incidencia);
        //Seleccionamos el campo a modificar
        int idx = -1;
        do {
            for (int i = 0; i < camposUpdate.length; i++) {
                System.out.printf("%d.- %s\n", i + 1, camposUpdate[i]);
            }
            idx = Utils.leerdato(idx, "Introduce selector de campo");
            idx--;
        } while ((idx < 0) || idx >= camposUpdate.length);

        switch (idx) {
            case 0:
                incidencia.setIdArea(new AreasDAO().seleccionar().getId());
                break;
            case 1:
                //Solicitamos Descripcion
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String descripcion = "";
                do {
                    descripcion = Utils.leerdato(descripcion, "Descripcion de la incidecnia");
                    descripcion = descripcion.trim();
                    if (descripcion.equals("")) {
                        System.out.println("\nLa descripcion no puede estar vacia\n");
                    }
                } while (descripcion.equals(""));
                incidencia.setDescripcion(descripcion);
                break;
            default: {
            }
        }

        //Modificamos la incidencia
        if (update(incidencia))
            System.out.printf("Modificacion correcta.\n%s\n", incidencia);
        else System.out.println("...parece que algo ha ido mal");

    }

    /**
     * Devuelve si la incidencia especificada esta cerrada consultando directamente
     * a la base de datos.
     *
     * @param id identificador de la Incidencia
     * @return true si esta cerrada. False sigue abierta o no existe.
     */
    public boolean isOpen(int id) {
        init();
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery = String.format("concat(/%s/%s[@%s=%d]/@Resuelta[1],\"\")",
                        myRoot, myNode, myIDField, id);
                ResourceSet result = servicio.query(myQuery);
                col.close();
                String tmp = String.valueOf(result.getResource(0L).getContent());
                return !tmp.equalsIgnoreCase("SI");
            } catch (Exception e) {
                System.out.println("Error al consultar.");
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
        return false;
    }

}
