package com.company.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {

    /**
     * Procedure that reads an String from console and returns it trimmed and adjusted to defined size
     * if String lenght is less than desired length it will be completed with blanks
     * @param size
     * @return String
     * @throws IOException
     * @Author Fran Orlando
     *
     */
    public static String readStrWithLeght(int size) throws IOException {

        //preparamos el buffer de lectura por consola
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String lectura= br.readLine();

        return setStringToFixedSize(lectura.trim(),size);
    }

    //Function that force an String to fit into a defined size
    public static String setStringToFixedSize(String str, int size){

        StringBuilder mistr= new StringBuilder();
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
}
