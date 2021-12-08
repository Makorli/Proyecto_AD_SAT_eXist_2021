package com.company.Controlador.Ficheros;

import com.company.Controlador.*;
import com.company.Modelos.Area;
import com.company.Modelos.Incidencia;
import com.company.Modelos.Tecnico;
import com.company.Modelos.Trabajo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *Clase para generacion de ficheros .dat de los modelos.
 * Leen los datos de una lista de objetos y lo guardan en un fichero utilizando
 * un flujo de datos de objetos
 */
public class EscribirFicheros_OOS {

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
    public static void FicheroTrabajos(){
        FicheroTrabajos.main(null);
    }
    public static void FicheroTipoIncidencias(){
        FicheroTipoIncidencias.main(null);
    }


    //INNER CLASSES

    /**
     * Clase de ecritura de la clase AreasEmpresa en el fichero e areas
     */
    private static class FicheroAreas {

        private static final String nombrefichero = "src/main/java/com/company/Data/Areas.dat";

        public static void main(String[] args) {
            //GENERAMOS EL FLUJO DE DATOS PARA ESCRITURA EN EL FICHERO
            ObjectOutputStream objectOutputStream = null;

            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(nombrefichero));

                //RECORREMOS LA LISTA
                for (Area a : AreasEmpresa.getLista()) {
                    objectOutputStream.writeObject(a);
                }
                //CERRAMOS FICHERO
                objectOutputStream.close();

            } catch (FileNotFoundException f) {
                System.out.format("Creando fichero no encontrado: %s\n", nombrefichero);
            } catch (IOException io) {
                System.out.format("Error E/S en %s\n", nombrefichero);
            }
        }
    }

    /**
     * Clase de ecritura de datos la clase Departamento Técnico en el fichero de tecnicos
     */
    private static class FicheroTecnicos {

        private static final String nombrefichero = "src/main/java/com/company/Data/Tecnicos.dat";

        public static void main(String[] args) {

            //GENERAMOS EL FLUJO DE DATOS PARA ESCRITURA EN EL FICHERO
            ObjectOutputStream objectOutputStream = null;

            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(nombrefichero));

                //RECORREMOS LA LISTA
                for (Tecnico a : DepartamentoTecnico.getLista()) {
                    objectOutputStream.writeObject(a);
                }
                //CERRAMOS FICHERO
                objectOutputStream.close();

            } catch (FileNotFoundException f) {
                System.out.format("Creando fichero no encontrado: %s\n", nombrefichero);
            } catch (IOException io) {
                System.out.format("Error E/S en %s\n", nombrefichero);
            }
        }
    }

    /**
     * Clase de ecritura de datos de la clase IncidenciasRepotadas en el fichero Incidencias
     */
    private static class FicheroIncidencias {

        private static final String nombrefichero = "src/main/java/com/company/Data/Incidencias.dat";

        public static void main(String[] args) {
            //GENERAMOS EL FLUJO DE DATOS PARA ESCRITURA EN EL FICHERO
            ObjectOutputStream objectOutputStream = null;

            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(nombrefichero));

                //RECORREMOS LA LISTA
                for (Incidencia a : IncidenciasReportadas.getLista()) {
                    objectOutputStream.writeObject(a);
                }
                //CERRAMOS FICHERO
                objectOutputStream.close();

            } catch (FileNotFoundException f) {
                System.out.format("Creando fichero no encontrado: %s\n", nombrefichero);
            } catch (IOException io) {
                System.out.format("Error E/S en %s\n", nombrefichero);
            }
        }
    }

    /**
     * Clase de ecritura de datos de la clase TRabajosRealizados en el fichero trabajos
     */
    private static class FicheroTrabajos {

        private static final String nombrefichero = "src/main/java/com/company/Data/Trabajos.dat";

        public static void main(String[] args) {
            //GENERAMOS EL FLUJO DE DATOS PARA ESCRITURA EN EL FICHERO
            ObjectOutputStream objectOutputStream = null;

            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(nombrefichero));

                //RECORREMOS LA LISTA
                for (Trabajo a : TrabajosRealizados.getLista()) {
                    objectOutputStream.writeObject(a);
                }
                //CERRAMOS FICHERO
                objectOutputStream.close();

            } catch (FileNotFoundException f) {
                System.out.format("Creando fichero no encontrado: %s\n", nombrefichero);
            } catch (IOException io) {
                System.out.format("Error E/S en %s\n", nombrefichero);
            }
        }
    }

    /**
     * Clase de ecritura de datos de la clase TiposDeIncidencia en el fichero Tipos de Incidencia
     */
    private static class FicheroTipoIncidencias {

        private static final String nombrefichero = "src/main/java/com/company/Data/TiposDeIncidencia.dat";

        public static void main(String[] args) {
            //GENERAMOS EL FLUJO DE DATOS PARA ESCRITURA EN EL FICHERO
            ObjectOutputStream objectOutputStream = null;

            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(nombrefichero));

                //RECORREMOS LA LISTA
                for (String a : TiposDeIncidencias.getLista()) {
                    objectOutputStream.writeObject(a);
                }
                //CERRAMOS FICHERO
                objectOutputStream.close();

            } catch (FileNotFoundException f) {
                System.out.format("Creando fichero no encontrado: %s\n", nombrefichero);
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

}
