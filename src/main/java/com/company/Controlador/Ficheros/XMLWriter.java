package com.company.Controlador.Ficheros;

import com.company.Controlador.*;
import com.company.Modelos.Area;
import com.company.Modelos.Incidencia;
import com.company.Modelos.Tecnico;
import com.company.Modelos.Trabajo;
import com.company.Utils.TecnicoCV;
import com.thoughtworks.xstream.XStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class XMLWriter {

    public static class ExportDepartamentoTecnico {

        private static String nombrefichero = "src/main/java/com/company/DataXML/Tecnicos.xml";

        public static void main(String[] args) {

            //Cargamos el fichero en memoria
            CargarFicheros_OOS.FicheroTecnicos();

            try{
                //instaciamos el Xstream
                XStream xstream = new XStream();
                xstream.registerConverter(new TecnicoCV());

                //Asociamos las etiquetas a las clases /atributos  correspondientes.
                xstream.alias("DepartamentoTecnico", List.class);
                xstream.alias("Tecnico", Tecnico.class);

                System.out.format("Generando fichero %s",nombrefichero);

                //Transformamos el fichero cargado en memoria
                xstream.toXML(DepartamentoTecnico.getLista(), new FileOutputStream(nombrefichero));

            } catch (FileNotFoundException f){
                System.out.println("Error E/S:"+ f.getMessage());
            }

        }

    }

    public static class ExportAreasEmpresa {

        private static String nombrefichero = "src/main/java/com/company/DataXML/Areas.xml";

        public static void main(String[] args) {

            //Cargamos el fichero en memoria
            CargarFicheros_OOS.FicheroAreas();

            try{
                //instaciamos el Xstream
                XStream xstream = new XStream();

                //Asociamos las etiquetas a las clases correspondientes.
                xstream.alias("AreasEmpresa", List.class);
                xstream.alias("Area", Area.class);

                System.out.format("Generando fichero %s\n",nombrefichero);

                //Transformamos el fichero cargado en memoria
                xstream.toXML(AreasEmpresa.getLista(), new FileOutputStream(nombrefichero));

            } catch (FileNotFoundException f){
                System.out.println("Error E/S: "+f.getMessage());
            }

        }

    }

    public static class ExportTiposIncidencias {

        private static String nombrefichero = "src/main/java/com/company/DataXML/TiposIncidencia.xml";

        public static void main(String[] args) {

            //Cargamos el fichero en memoria
            CargarFicheros_OOS.FicheroTipoIncidencias();

            try{
                //instaciamos el Xstream
                XStream xstream = new XStream();

                //Asociamos las etiquetas a las clases correspondientes.
                xstream.alias("TiposDeIncidencia", List.class);
                xstream.alias("Tipo", Incidencia.class);

                //Eliminamos las etiquetas /atributos de la clase que no queremos que aparezcan
                //xstream.addImplicitCollection(DepartamentoTecnico.class,"tecnicoMap");

                System.out.format("Generando fichero %s",nombrefichero);

                //Transformamos el fichero cargado en memoria
                xstream.toXML(TiposDeIncidencias.getLista(), new FileOutputStream(nombrefichero));

            } catch (FileNotFoundException f){
                System.out.println("Error E/S"+f.getMessage());
            }

        }

    }

    public static class ExportIncidenciasReportadas {

        private static String nombrefichero = "src/main/java/com/company/DataXML/Incidencias.xml";

        public static void main(String[] args) {

            //Cargamos el fichero en memoria
            CargarFicheros_OOS.FicheroIncidencias();

            try{
                //instaciamos el Xstream
                XStream xstream = new XStream();

                xstream.processAnnotations(Incidencia.class);
                xstream.alias("Incidencias Reportadas", List.class);

                System.out.format("Generando fichero %s",nombrefichero);

                //Transformamos el fichero cargado en memoria
                xstream.toXML(IncidenciasReportadas.getLista(), new FileOutputStream(nombrefichero));

            } catch (FileNotFoundException f){
                System.out.println("Error E/S"+f.getMessage());
            }

        }

    }

    public static class ExportIncidenciasPendientes {

        private static String nombrefichero = "src/main/java/com/company/DataXML/IncidenciasPendientes.xml";

        public static void main(String[] args) {

            //Cargamos el fichero en memoria
            CargarFicheros_OOS.FicheroIncidencias();

            try{
                //instaciamos el Xstream
                XStream xstream = new XStream();

                //Asociamos las etiquetas a las clases correspondientes.
                xstream.alias("IncidenciasReportadas", List.class);
                xstream.alias("Incidencia", Incidencia.class);

                System.out.format("Generando fichero %s",nombrefichero);

                //Transformamos el fichero cargado en memoria
                xstream.toXML(IncidenciasReportadas.getIncidenciasNoCerradasList(), new FileOutputStream(nombrefichero));

            } catch (FileNotFoundException f){
                System.out.println("Error E/S"+f.getMessage());
            }

        }

    }

    public static class ExportIncidenciasCerradas {

        private static String nombrefichero = "src/main/java/com/company/DataXML/IncidenciasCerradas.xml";

        public static void main(String[] args) {

            //Cargamos el fichero en memoria
            CargarFicheros_OOS.FicheroIncidencias();

            try{
                //instaciamos el Xstream
                XStream xstream = new XStream();

                //Asociamos las etiquetas a las clases correspondientes.
                xstream.alias("IncidenciasReportadas", List.class);
                xstream.alias("Incidencia", Incidencia.class);

                System.out.format("Generando fichero %s",nombrefichero);

                //Transformamos el fichero cargado en memoria
                xstream.toXML(IncidenciasReportadas.getIncidenciasCerradasList(), new FileOutputStream(nombrefichero));

            } catch (FileNotFoundException f){
                System.out.println("Error E/S"+f.getMessage());
            }

        }

    }

    public static class ExportTrabajosRealizados {

        private static String nombrefichero = "src/main/java/com/company/DataXML/TrabajosRealizados.xml";

        public static void main(String[] args) {

            //Cargamos el fichero en memoria
            CargarFicheros_OOS.FicheroTrabajos();

            try{
                //instaciamos el Xstream
                XStream xstream = new XStream();

                xstream.processAnnotations(Trabajo.class);
                xstream.alias("Trabajos", List.class);

                System.out.format("Generando fichero %s",nombrefichero);

                //Transformamos el fichero cargado en memoria
                xstream.toXML(TrabajosRealizados.getLista(), new FileOutputStream(nombrefichero));

            } catch (FileNotFoundException f){
                System.out.println("Error E/S"+f.getMessage());
            }

        }
    }

}
