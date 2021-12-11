package com.company.Main;

import com.company.Controlador.DBController;
import com.company.Controlador.Ficheros.XMLWriter;
import com.company.Controlador.IncidenciasDAO;
import com.company.Generador;
import com.company.Modelos.Incidencia;
import com.thoughtworks.xstream.XStream;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XPathQueryService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class Main {

    //Función genérica de lectura de datos
    private static <T> T leerdato(T o, String textoDescripcion) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String tipodato = o.getClass().getSimpleName();
        textoDescripcion = textoDescripcion == null ? "" : textoDescripcion;

        switch (tipodato.toUpperCase()) {

            case "INTEGER":
                try {
                    System.out.format("Introduce %s: ", (textoDescripcion.isEmpty()) ? "dato" : textoDescripcion);
                    o = (T) Integer.valueOf(br.readLine());
                } catch (NumberFormatException | IOException e) {
                    System.out.format("Dato %s introducido no correcto\n", (textoDescripcion.isEmpty()) ? tipodato : textoDescripcion);
                }
                break;
            case "STRING":
                try {
                    System.out.format("Introduce %s: ", (textoDescripcion.isEmpty()) ? "dato" : textoDescripcion);
                    o = (T) br.readLine();
                } catch (IOException e) {
                    System.out.format("Dato %s introducido no correcto\n", (textoDescripcion.isEmpty()) ? tipodato : textoDescripcion);
                }
                break;
            case "DOUBLE":
                try {
                    System.out.format("Introduce %s: ", (textoDescripcion.isEmpty()) ? "dato" : textoDescripcion);
                    o = (T) Double.valueOf(br.readLine());
                } catch (NumberFormatException | IOException e) {
                    System.out.format("Dato %s introducido no correcto\n", (textoDescripcion.isEmpty()) ? tipodato : textoDescripcion);
                }
                break;
            default:
                System.out.println("ERROR.Tipo de dato no reconocido");
        }
        return o;
    }

    private static final String dbCol = "TEST-PROYECTO";

    public static void main(String[] args) {
        // write your code here
        String opcion = "";
        Scanner sc = new Scanner(System.in);

        DBController.setDeFaultCollection(dbCol);
        IncidenciasDAO cti = new IncidenciasDAO();
        do {
            //MONTAMOS EL MENÚ

            System.out.println(
                    "\n1. Generar y cargar Datos de Prueba.\n" +
                            "2. Insertar Incidencia\n" +
                            "3. Modificar Incidencia\n" +
                            "4. Cerrar Incidencia\n" +
                            "5. Imputar horas Incidencia\n" +
                            "6. Eliminar Incidencia\n" +
                            "7. Listar todas las incidencia\n" +
                            "8. Consultas\n" +

                            "0. Salir\n"
            );

            opcion = leerdato(opcion, "opcion").toUpperCase();

            int nIncidencias = 0;
            switch (opcion) {

                // CARGA DE DATOS INICIALES

                case "1":   //Generacion Interactiva y carga de datos de prueba.

                    //GENERAMOS DATOS DE PRUEBA Y LOS GUARDAMOS EN FICHEROS .DAT
                    System.out.println("Cargando Datos de Prueba");
                    Generador.DatosParaPruebas();
                    Generador.NuevasIncidencias(100);
                    Generador.AtenderIncidencias(100);

                    //EXPORTACION DE DATOS A XML
                    XMLWriter.ExportDepartamentoTecnico.main(new String[]{});
                    XMLWriter.ExportIncidenciasReportadas.main(new String[]{});
                    XMLWriter.ExportIncidenciasCerradas.main(new String[]{});
                    XMLWriter.ExportIncidenciasPendientes.main(new String[]{});
                    XMLWriter.ExportAreasEmpresa.main(new String[]{});
                    XMLWriter.ExportTiposIncidencias.main(new String[]{});
                    XMLWriter.ExportTrabajosRealizados.main(new String[]{});

                    //SUBIDA DE XMLS EXPORTADOS A COLLECION EXISTENTE
                    DBController.init();
                    DBController.setDeFaultCollection(dbCol);
                    //DBController.createCollection(dbCol);
                    DBController.addFilesToCollection(
                            new File("src/main/java/com/company/DataXML/Incidencias.xml"),
                            dbCol);
                    DBController.addFilesToCollection(
                            new File("src/main/java/com/company/DataXML/TrabajosRealizados.xml"),
                            dbCol);
                    DBController.addFilesToCollection(
                            new File("src/main/java/com/company/DataXML/Tecnicos.xml"),
                            dbCol);
                    DBController.addFilesToCollection(
                            new File("src/main/java/com/company/DataXML/Areas.xml"),
                            dbCol);
                    break;
                //Insertar Incidencia
                case "2":
                    Incidencia incidencia = cti.crear();
                    cti.insert(incidencia);
                    break;
                //Modificar Incidencia
                case "3":
                    cti.modificar();
                    break;
                //Cerrar Incidencia
                case "4":
                    //TODO
                    break;
                //Imputar Horas incidencia
                case "5":
                    //TODO
                    break;
                //Eliminar Incidencia
                case "6":
                    cti.delete(cti.seleccionar());
                    break;
                //Listar todas las incidencias
                case "7":
                    List<Incidencia> myList = cti.getAllObjects();
                    for (Incidencia i : myList) {
                        System.out.println(i);
                    }
                //Consultas
                case "8":
                    //TODO
                    break;
                //SALIR DEL PROGRAMA

                case "10":
                    System.out.println("Bye Bye");
                    break;

                //OPCION INCORRECTA
                default:
                    System.out.println("Opcion incorrecta");
            }
        } while (!(opcion.equals("5")));

    }

    private static void insertarIncidencia(Incidencia incidencia, String colName) {

        XStream xstream = new XStream();
        xstream.processAnnotations(Incidencia.class);
        String inciXML = xstream.toXML(incidencia);

        Collection col = DBController.getCollectionFromDB(colName);
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                System.out.printf("Inserto: %s \n", incidencia.getId());
                //Consulta para insertar --> update insert ... into
                ResourceSet result = servicio.query("update insert " + inciXML + " into /IncidenciasReportadas");
                System.out.println("MIRA ESTO -->" + result);
                col.close(); //borramos
                System.out.println("Insertado.");
            } catch (Exception e) {
                System.out.println("Error al Insertar.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
    }

    private static boolean comprobarId(int id, String colName) {

        Collection col = DBController.getCollectionFromDB(colName);
        if (col != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                //Consulta para consultar la información de un departamento
                String myQuery = String.format("/IncidenciasReportadas/Incidencia[@Id='%d']", id);
                ResourceSet result = servicio.query(myQuery);
                ResourceIterator i;
                i = result.getIterator();
                col.close();
                if (!i.hasMoreResources()) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Error al consultar.");
                // e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }

        return false;

    }// comprobardep

    private static void modificarincidencia(Incidencia incidencia, String colName) {

        int idIncidencia = incidencia.getId();

        if (comprobarId(idIncidencia, colName)) {

            Collection col = DBController.getCollectionFromDB(colName);
            if (col != null) {
                try {
                    System.out.printf("Actualizo la incidencia: %s\n", idIncidencia);
                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    //Consulta para modificar/actualizar un valor --> update value

                    String myQuery = String
                            .format("update value /IncidenciasReportadas/Incidencia[@Id='%d']/horas with data(%d)",
                                    incidencia.getId(),
                                    incidencia.getHoras());

                    ResourceSet result = servicio.query(myQuery);

                    col.close();
                    System.out.println("Dep actualizado.");
                } catch (Exception e) {
                    System.out.println("Error al actualizar.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println("El departamento NO EXISTE.");
        }
    }

    private static void borrarIncidencia(Incidencia incidencia, String colName) {
        int idIncidencia = incidencia.getId();

        if (comprobarId(idIncidencia, colName)) {

            Collection col = DBController.getCollectionFromDB(colName);
            if (col != null) {
                try {
                    System.out.printf("Borro la incidencia: %s\n", incidencia.getId());
                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    //Consulta para borrar un departamento --> update delete
                    String myQuery = String
                            .format("update delete /IncidenciasReportadas/Incidencia[@Id='%d']",
                                    incidencia.getId()
                            );
                    ResourceSet result = servicio.query(myQuery);
                    col.close();
                    System.out.println("Incidencia borrada.");
                } catch (Exception e) {
                    System.out.println("Error al borrar.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println("El departamento NO EXISTE.");
        }

    }

    /**
     * Procedmiento de lectura y creación de una incidencia.
     *
     * @return
     */
    public Incidencia crear() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Incidencia incidencia = new Incidencia();

        String area = "";
        String descripcion = "";
        try {
            System.out.println("Area que reporta la incidencia?: ");
            area = br.readLine();
            System.out.println("Descipción de la incidencia: ");
            descripcion = br.readLine();
        } catch (IOException e) {
            System.out.println("Error en la lectura de datos");
        }
        //TODO asignacion y comrobacion de campos leidos

        return incidencia;
    }
}

