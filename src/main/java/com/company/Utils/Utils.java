package com.company.Utils;

import com.company.Modelos.Incidencia;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    /**
     * Procedure that reads an String from console and returns it trimmed and adjusted to defined size
     * if String lenght is less than desired length it will be completed with blanks
     *
     * @param size
     * @return String
     * @throws IOException
     * @Author Fran Orlando
     */
    public static String readStrWithLeght(int size) throws IOException {

        //preparamos el buffer de lectura por consola
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String lectura = br.readLine();

        return setStringToFixedSize(lectura.trim(), size);
    }

    //Function that force an String to fit into a defined size
    public static String setStringToFixedSize(String str, int size) {

        StringBuilder mistr = new StringBuilder();
        mistr.append(str);  //añadimos a StringBuffer
        mistr.setLength(size);  //redimensionamos
        return mistr.toString();
    }

    //Función genérica de lectura de datos
    public static <T> T leerdato(T o, String textoDescripcion) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String tipodato = o.getClass().getSimpleName();
        textoDescripcion = textoDescripcion == null ? "" : textoDescripcion;

        switch (tipodato.toUpperCase()) {

            case "INTEGER":
                try {
                    System.out.format("%s: ", (textoDescripcion.isEmpty()) ? "dato" : textoDescripcion);
                    o = (T) Integer.valueOf(br.readLine());
                } catch (NumberFormatException | IOException e) {
                    System.out.format("%s no es correcto\n", (textoDescripcion.isEmpty()) ? tipodato : textoDescripcion);
                }
                break;
            case "STRING":
                try {
                    System.out.format("%s: ", (textoDescripcion.isEmpty()) ? "dato" : textoDescripcion);
                    o = (T) br.readLine();
                } catch (IOException e) {
                    System.out.format("%s no es correcto\n", (textoDescripcion.isEmpty()) ? tipodato : textoDescripcion);
                }
                break;
            case "DOUBLE":
                try {
                    System.out.format("%s: ", (textoDescripcion.isEmpty()) ? "dato" : textoDescripcion);
                    o = (T) Double.valueOf(br.readLine());
                } catch (NumberFormatException | IOException e) {
                    System.out.format("%s no es correcto\n", (textoDescripcion.isEmpty()) ? tipodato : textoDescripcion);
                }
                break;

            default:
                System.out.println("ERROR.Tipo de dato no reconocido");
        }
        return o;
    }

    //Clase para la ejecucion de consultas de ficheros
    public static class XQuerysFromFiles {


        public List<Incidencia> getIncidenciasListResult(String queryFile, Collection collection) {
            List<Incidencia> myList = new ArrayList<>();
            try {
                //Leer el fichero y guardarlo en una cadena
                BufferedReader entrada = new BufferedReader(new FileReader(queryFile));
                String linea = null;
                StringBuilder stringBuilder = new StringBuilder();
                String salto = System.getProperty("line.separator");
                while ((linea = entrada.readLine()) != null) {
                    stringBuilder.append(linea);
                    stringBuilder.append(salto);
                }
                String consulta = stringBuilder.toString();

                //Ejecutar la consulta y devolver resultado
                XPathQueryService servicio = (XPathQueryService) collection
                        .getService("XPathQueryService", "1.0");
                ResourceSet result = servicio.query(consulta);
                ResourceIterator i;
                i = result.getIterator();

                //Preparamos conversion de XML a objetos
                XStream xStream = new XStream();
                xStream.alias("Incidencia", Incidencia.class);
                xStream.addPermission(AnyTypePermission.ANY);       //Permtimos los permisos de desaserializacion
                xStream.registerConverter(new IncidenciasCV());     //Asignamos conversor

                //Iterar el resultado y convertir a objetos
                while (i.hasMoreResources()) {
                    Resource r = i.nextResource();
                    String objectXML = (String) r.getContent();
                    Incidencia incidencia = (Incidencia) xStream.fromXML(objectXML);
                    myList.add(incidencia);
                }
                collection.close();
                return myList;

            } catch (XMLDBException ex) {
                ex.printStackTrace();
            } catch (FileNotFoundException ex) {
                System.out.println("Error fichero no encontrado: " + queryFile);
            } catch (IOException ex) {
                ex.printStackTrace();

            }
            return myList;
        }

    }
}