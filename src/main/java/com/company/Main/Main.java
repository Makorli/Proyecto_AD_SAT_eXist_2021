package com.company.Main;

import com.company.Controlador.DBController;
import com.company.Controlador.Ficheros.XMLWriter;
import com.company.Generador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {

    //Función genérica de lectura de datos
    private static <T>T leerdato(T o, String textoDescripcion) {
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

    public static void main(String[] args) {
        // write your code here

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String opcion = "";

        Scanner sc = new Scanner(System.in);
        do {
            //MONTAMOS EL MENÚ

            System.out.println(
                    "1. Generar y cargar Datos de Prueba.\n" +
                            "2. Generar y cargar Datos de Prueba.\n" +
                            "5. Salir\n"
            );

            opcion = leerdato(opcion, "opcion").toUpperCase();

            int nIncidencias=0;
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
                    break;

                    //SUBIDA DE XMLS EXPORTADOS A COLLECION EXISTENTE

                case "2":
                    DBController.init();
                    break;

                //SALIR DEL PROGRAMA

                case "5":
                    System.out.println("Bye Bye");
                    break;

                //OPCION INCORRECTA
                default:
                    System.out.println("Opcion incorrecta");
            }
            //PAUSA PARA VER LISTADOS Y SALIDA DE CONSOLA
            System.out.println("\nPulsa una tecla para continuar....");
            sc.nextLine();
        } while (!(opcion.equals("5")));

    }
}

