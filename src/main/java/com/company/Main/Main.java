package com.company.Main;

import com.company.Controlador.AreasDAO;
import com.company.Controlador.DBController;
import com.company.Controlador.Ficheros.XMLWriter;
import com.company.Controlador.IncidenciasDAO;
import com.company.Controlador.Generador;
import com.company.Modelos.Area;
import com.company.Modelos.Incidencia;
import com.company.Utils.Utils;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String dbCol = "TEST-PROYECTO";

    public static void main(String[] args) {

        DBController.setDeFaultCollection(dbCol);
        IncidenciasDAO cti = new IncidenciasDAO();

        String opcion = "";
        Scanner sc = new Scanner(System.in);
        do {
            showMainMenu();
            opcion = Utils.leerdato(opcion, "Opcion").toUpperCase();

            switch (opcion) {

                // CARGA DE DATOS INICIALES

                case "1":   //Generacion Interactiva y carga de datos de prueba.
                {
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
                }
                case "2":   //Insertar Incidencia
                {
                    Incidencia incidencia = cti.crear();
                    cti.insert(incidencia);
                    break;
                }
                case "3":   //Modificar Incidencia
                {
                    cti.modificar();
                    break;
                }
                case "4":   //Cerrar Incidencia
                {
                    cti.closeIncidencia(cti.seleccionar().getId(), null);
                    break;
                }
                case "5":   //Imputar Horas incidencia
                {
                    cti.imputarHoras(cti.seleccionar().getId());
                    break;
                }
                case "6":   //Eliminar Incidencia
                {
                    cti.delete(cti.seleccionar());
                    break;
                }
                case "7":   //Listar todas las incidencias
                {
                    List<Incidencia> myList = cti.getAllObjects();
                    for (Incidencia i : myList) {
                        System.out.println(i);
                    }
                    break;
                }
                case "8":   //Consultas
                {
                    String opcion8 = "";
                    do {
                        showQueryMenu();
                        opcion8 = Utils.leerdato(opcion, "Opcion").toUpperCase();
                        switch (opcion8) {
                            //Salir Menu
                            case "0": {
                                System.out.println("Menu Principal");
                                break;
                            }
                            //Consulta incidencias por Area y estado a XML
                            case "1": {
                                String lectura = "";
                                boolean todok = false;
                                boolean estado = false;
                                do {
                                    lectura = Utils.leerdato(lectura, "Introduce estado de incidencia -> Cerradas (C) / Abiertas (A)");
                                    if (lectura.equalsIgnoreCase("A") ||
                                            lectura.equalsIgnoreCase("Abiertas")) {
                                        estado = false;
                                        todok = true;
                                    } else if (lectura.equalsIgnoreCase("C") ||
                                            lectura.equalsIgnoreCase("Cerradas")) {
                                        estado = true;
                                        todok = true;
                                    }
                                } while (!todok);
                                System.out.println("\n");
                                Area area = new AreasDAO().seleccionar();
                                List<Incidencia> incidenciaList = new IncidenciasDAO()
                                        .getAllObjectsByStateAndArea(estado, area.getId());

                                //EXPORTAMOS LA LISTA A UN FICHERO XML
                                //MOSTRAMOS LA LISTA EN PANTALLA
                                String nombrefichero = "src/main/java/com/company/ExportedXMLData/ConsultaEstadoAreas.xml";
                                try {
                                    XStream xstream = new XStream();
                                    xstream.processAnnotations(Incidencia.class);
                                    xstream.alias("IncidenciasReportadas", List.class);
                                    System.out.format("\nListado exportado en fichero %s\n", nombrefichero);
                                    xstream.toXML(incidenciaList, new FileOutputStream(nombrefichero));

                                } catch (FileNotFoundException f) {
                                    System.out.println("Error E/S" + f.getMessage());
                                }

                                break;
                            }
                            //Top Incidencias abiertas con más horas a XML
                            case "2": {
                                Utils.XQuerysFromFiles xq= new Utils.XQuerysFromFiles();
                                List<Incidencia> myList = xq.getIncidenciasListResult(
                                        "src/main/java/com/company/XMLQuerys/Top10Incidencias.xq",
                                        DBController.getDefaultCollection());

                                //EXPORTAMOS LA LISTA A UN FICHERO XML
                                String nombrefichero = "src/main/java/com/company/ExportedXMLData/Top10IncidenciasHoras.xml";
                                try {
                                    XStream xstream = new XStream();
                                    xstream.processAnnotations(Incidencia.class);
                                    xstream.alias("IncidenciasReportadas", List.class);
                                    System.out.format("\nListado exportado en fichero %s\n", nombrefichero);
                                    xstream.toXML(myList, new FileOutputStream(nombrefichero));

                                } catch (FileNotFoundException f) {
                                    System.out.println("Error E/S" + f.getMessage());
                                }

                                //MOSTRAMOS EN PANTALLA EL RESULTADO
                                System.out.println("\n LISTADO TOP 10 INCIDENCIAS CON MAS HORAS");
                                for (Incidencia i: myList){
                                    System.out.println(i);
                                }

                                break;
                            }
                            default: System.out.println("Opcion incorrecta");
                        }

                    } while (!(opcion8.equals("0")));
                    break;
                }
                //SALIR DEL PROGRAMA

                case "0":
                    System.out.println("Bye Bye");
                    break;

                //OPCION INCORRECTA
                default:
                    System.out.println("Opcion incorrecta");
            }
        } while (!(opcion.equals("0")));

    }

    private static void showMainMenu() {
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
    }

    private static void showQueryMenu() {
        System.out.println(
                "\n1. Consulta incidencias por Area y estado a XML\n" +
                        "2. Top 10 Incidencias abiertas con más horas a XML (desde fichero .xq)\n" +
                        "0. Salir\n"
        );
    }


}

