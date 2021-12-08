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


}
