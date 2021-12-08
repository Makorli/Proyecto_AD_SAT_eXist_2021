package com.company.Controlador.Ficheros;

import com.company.Controlador.*;
import com.company.Modelos.Area;
import com.company.Modelos.Incidencia;
import com.company.Modelos.Tecnico;
import com.company.Modelos.Trabajo;

import java.io.*;

/**
 *Clase para carga de ficheros .dat en las clases de almacenamiento que contienen las listas
 * Leen los datos de un fichero y lo guardan en la lista de objetos correspondiente utilizando
 * un flujo de datos de objetos
 */
public class CargarFicheros_OOS {

    //METODOS DE EJECUCION PUBLICOS DE LAS CLASES
    public static void FicheroAreas(){
        FicheroAreas.main(null);
    }
    public static void FicheroTecnicos(){
        FicheroTecnicos.main(null);
    }
    public static void FicheroIncidencias(){
        FicheroIncidencias.main(null);
    }
    public static void FicheroTrabajos(){ FicheroTrabajos.main(null); }
    public static void FicheroTipoIncidencias(){
        FicheroTipoIncidencias.main(null);
    }


    /**
     * Clase de lectura y carga de datos  de un fichero la clase Areas Empesa
     */
    private static class FicheroAreas{

        private static final String nombrefichero = "src/main/java/com/company/Data/Areas.dat";

        public static void main(String[] args) {
            //GENERAMOS EL FLUJO DE DATOS PARA LECTURA DEL FICHERO
            ObjectInputStream objectInputStream = null;

            try {
                objectInputStream = new ObjectInputStream(new FileInputStream(nombrefichero));

                //RECORREMOS EL FICHERO Y LO CARGAMOS EN LA LISTA CORRESPONDIENTE
                Area a = null;
                AreasEmpresa.getMap().clear();
                try{
                    while (true){
                        a = (Area) objectInputStream.readObject();
                        AreasEmpresa.add(a);
                    }
                }
                catch(EOFException e){
                    System.out.format("Fin de carga fichero %s\n",nombrefichero);
                }  //Salimos del bucle de lectura.
                catch (ClassNotFoundException e) {
                    System.out.println("Clase almacenada incorrecta");
                }

                //CERRAMOS FICHERO
                objectInputStream.close();

            } catch (FileNotFoundException f) {
                System.out.format("Fichero %s no encontrado\n", nombrefichero);
            } catch (IOException io) {
                System.out.format("Error E/S en %s\n", nombrefichero);
            }
        }
    }

    /**
     * Clase de lectura y carga de datos  del fichero de tecnicos en la clase Departamento Técnico
     */
    private static class FicheroTecnicos {

        private static final String nombrefichero = "src/main/java/com/company/Data/Tecnicos.dat";

        public static void main(String[] args) {
            //GENERAMOS EL FLUJO DE DATOS PARA LECTURA DEL FICHERO
            ObjectInputStream objectInputStream = null;

            try {
                objectInputStream = new ObjectInputStream(new FileInputStream(nombrefichero));

                //RECORREMOS EL FICHERO Y LO CARGAMOS EN LA LISTA CORRESPONDIENTE
                Tecnico a = null;
                DepartamentoTecnico.getMap().clear();
                try{
                    boolean primeravez=true;
                    while (true){
                        a = (Tecnico) objectInputStream.readObject();
                        DepartamentoTecnico.add(a);
                    }
                }
                catch(EOFException e){
                    System.out.format("Fin de carga fichero %s\n",nombrefichero);
                }  //Salimos del bucle de lectura.
                catch (ClassNotFoundException e) {
                    System.out.println("Clase almacenada incorrecta");
                }

                //CERRAMOS FICHERO
                objectInputStream.close();

            } catch (FileNotFoundException f) {
                System.out.format("Fichero %s no encontrado\n", nombrefichero);
            } catch (IOException io) {
                System.out.format("Error E/S en %s\n", nombrefichero);
            }
        }
    }

    /**
     * Clase de lectura y carga de datos  del fichero de incidencias en la clase IncidenciasReportadas
     */
    private static class FicheroIncidencias {

        private static final String nombrefichero = "src/main/java/com/company/Data/Incidencias.dat";

        public static void main(String[] args) {
            //GENERAMOS EL FLUJO DE DATOS PARA LECTURA DEL FICHERO
            ObjectInputStream objectInputStream = null;

            try {
                objectInputStream = new ObjectInputStream(new FileInputStream(nombrefichero));

                //RECORREMOS EL FICHERO Y LO CARGAMOS EN LA LISTA CORRESPONDIENTE
                Incidencia a = null;
                IncidenciasReportadas.getMap().clear();
                IncidenciasReportadas.getIncidenciasNoCerradasList().clear();
                IncidenciasReportadas.getIncidenciasCerradasList().clear();
                try{
                    IncidenciasReportadas.getMap().clear();
                    while (true){
                        a = (Incidencia) objectInputStream.readObject();
                        IncidenciasReportadas.add(a);
                    }
                }
                catch(EOFException e){
                    System.out.format("Fin de carga fichero %s\n",nombrefichero);
                }  //Salimos del bucle de lectura.
                catch (ClassNotFoundException e) {
                    System.out.println("Clase almacenada incorrecta");
                }

                //CERRAMOS FICHERO
                objectInputStream.close();

            } catch (FileNotFoundException f) {
                System.out.format("Fichero %s no encontrado\n", nombrefichero);
            } catch (IOException io) {
                System.out.format("Error E/S en %s\n", nombrefichero);
            }
        }
    }

    /**
     * Clase de lectura y carga de datos  del fichero de trabajos en la clase de trabajosREalizados
     */
    private static class FicheroTrabajos {

        private static final String nombrefichero = "src/main/java/com/company/Data/Trabajos.dat";

        public static void main(String[] args) {
            //GENERAMOS EL FLUJO DE DATOS PARA LECTURA DEL FICHERO
            ObjectInputStream objectInputStream = null;

            try {
                objectInputStream = new ObjectInputStream(new FileInputStream(nombrefichero));

                //RECORREMOS EL FICHERO Y LO CARGAMOS EN LA LISTA CORRESPONDIENTE
                Trabajo a = null;
                TrabajosRealizados.getLista().clear();
                try{
                    while (true){
                        a = (Trabajo) objectInputStream.readObject();
                        TrabajosRealizados.add(a);
                    }
                }
                catch(EOFException e){  }  //Salimos del bucle de lectura.
                catch (ClassNotFoundException e) {
                    System.out.println("Clase almacenada incorrecta");
                }

                //CERRAMOS FICHERO
                objectInputStream.close();

            } catch (FileNotFoundException f) {
                System.out.format("Fichero %s no encontrado\n", nombrefichero);
            } catch (IOException io) {
                System.out.format("Error E/S en %s\n", nombrefichero);
            }
        }
    }

    /**
     * Clase de lectura y carga de datos del ficheto de TiposDeIncidencia a la calse TiposDeIncidencia
     */
    private static class FicheroTipoIncidencias {

        private static final String nombrefichero = "src/main/java/com/company/Data/TiposDeIncidencia.dat";

        public static void main(String[] args) {
            //GENERAMOS EL FLUJO DE DATOS PARA LECTURA DEL FICHERO
            ObjectInputStream objectInputStream = null;

            try {
                objectInputStream = new ObjectInputStream(new FileInputStream(nombrefichero));

                //RECORREMOS EL FICHERO Y LO CARGAMOS EN LA LISTA CORRESPONDIENTE
                String a = null;
                TiposDeIncidencias.getLista().clear();
                try{
                    while (true){
                        a = (String) objectInputStream.readObject();
                        TiposDeIncidencias.add(a);
                    }
                }
                catch(EOFException e){
                    System.out.format("Fin de carga fichero %s\n",nombrefichero);
                }  //Salimos del bucle de lectura.
                catch (ClassNotFoundException e) {
                    System.out.println("Clase almacenada incorrecta");
                }

                //CERRAMOS FICHERO
                objectInputStream.close();

            } catch (FileNotFoundException f) {
                System.out.format("Fichero %s no encontrado\n", nombrefichero);
            } catch (IOException io) {
                System.out.format("Error E/S en %s\n", nombrefichero);
            }
        }
    }

    public static void main(String[] args) {
        FicheroAreas();
        FicheroTecnicos();
        FicheroIncidencias();
        FicheroTrabajos();
        FicheroTipoIncidencias();
    }

    public static void cargarTodosLosFicheros(){
        FicheroAreas();
        FicheroTecnicos();
        FicheroIncidencias();
        FicheroTrabajos();
        FicheroTipoIncidencias();
    }
}
