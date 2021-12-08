package com.company;

import com.company.Controlador.*;
import com.company.Controlador.Ficheros.CargarFicheros_OOS;
import com.company.Controlador.Ficheros.EscribirFicheros_OOS;
import com.company.Modelos.Area;
import com.company.Modelos.Incidencia;
import com.company.Modelos.Tecnico;
import com.company.Modelos.Trabajo;
import com.company.Utils.TechSpecialization;

import java.util.Random;

public class Generador {

    public static void NuevasIncidencias(int nIncidencias) {
        NuevasIncidencias.main(new String[]{String.valueOf(nIncidencias)});
    }

    public static void AtenderIncidencias(int nTrabajos) {
        AtenderIncidencias.main(new String[]{String.valueOf(nTrabajos)});
    }

    public static void DatosParaPruebas() {
        DatosParaPruebas.main(new String[]{});
    }

    /**
     * Generador aleatorio de incidencias en los departamentos.
     * La descripcino de la incidencia se realiza de manera genéricas segun la
     */
    private static class NuevasIncidencias {

        public static void main(String[] args) {

            int nIncidenciasNuevas=-1; // -1 aleatorio

            if (args.length != 0 ) {
                nIncidenciasNuevas = (Integer.parseInt(args[0]));
                if (nIncidenciasNuevas < 0)
                    nIncidenciasNuevas = new Random().nextInt(14)+1;
            }

            //COMPROBAMOS QUE TENEMOS LOS DATOS NECESARIOS PARA GENERAR LAS INCIDENCIAS ALEATORIAMENTE
            //SI NO LOS TENEMOS EN MEMORIA LO CARGAMOS DE LOS FICHEROS Y SI NO HAY DATOS DEVOLVEMOS ERROR

            // Comprobacion Tipos De incidencias para la descripcion
            boolean check1Ok = false;
            if (TiposDeIncidencias.getLista().size() == 0) {
                CargarFicheros_OOS.FicheroTipoIncidencias();
                //Si la carga ha ido bien correcto
                if (TiposDeIncidencias.getLista().size() > 0) check1Ok = true;
            } else check1Ok = true;

            //Comprobacion de Areas
            boolean check2Ok = false;
            if (AreasEmpresa.getLista().size() == 0) {
                CargarFicheros_OOS.FicheroAreas();
                //Si la carga ha ido bien correcto
                if (AreasEmpresa.getLista().size() > 0) check2Ok = true;
            } else check2Ok = true;

            //Si TENEMOS LOS DATOS MINIMOS PARA GENERAR INCIDENCIAS ALEATORIAMENTE, NOS PONEMOS A ELLO

            if (check1Ok && check2Ok) {

                //CARgamos las incidencias existentes. Si las hay..
                CargarFicheros_OOS.FicheroIncidencias();

                Random rnd = new Random(); //generador de aleatorios
                int incIdxSel;
                int areaIdxSel;

                System.out.format("\nReportando %d incidencias nuevas\n\n", nIncidenciasNuevas);

                for (int i = 0; i < nIncidenciasNuevas; i++) {
                    incIdxSel = rnd.nextInt(TiposDeIncidencias.getLista().size()); //Tipo de incidencia
                    areaIdxSel = rnd.nextInt(AreasEmpresa.getLista().size()); // Area de que registra la incidencia.

                    //GENERAMOS UNA NUEVA INCIDENCIA CON LOS DATOS ALEATORIOS Y LA GUARDAMOS EN LA LISTA DE INCIDENCIAS
                    Incidencia nuevaIncidencia = new Incidencia(
                            IncidenciasReportadas.getFreeId(),
                            TiposDeIncidencias.getLista().get(incIdxSel),
                            AreasEmpresa.getLista().get(areaIdxSel).getId()
                    );

                    //Mostramos la Incidencia en pantalla
                    System.out.format("Incidencia: %-30s\tArea: %s\n",
                            nuevaIncidencia.getDescripcion(),
                            nuevaIncidencia.getIdArea());

                    //Añadimos la nueva incidencia a las incidencias Reportadas
                    IncidenciasReportadas.add(nuevaIncidencia);
                }

                //Anexamos las nuevas incidencias al fichero de Incidencias
                EscribirFicheros_OOS.FicheroIncidencias();

            } else System.out.println("Imposible generar nuevas incidencias, problemas con los ficheros.");


        }

    }

    /**
     * Clase para aleatorizar gestionar la atención de incidencias abiertas por parte de los
     * técnicos existentes.
     */
    private static class AtenderIncidencias {

        //Generador aleatorio de trabajos y atencion de incidencias abiertas.
        public static void main(String[] args) {

            int nTrabajosMax;
            int nTrabajos=20;

            if (args.length != 0 ) {
                nTrabajos = (Integer.parseInt(args[0]));
            }


            //COMPROBAMOS QUE TENEMOS LOS DATOS NECESARIOS PARA ATENDER LAS INCIDENCIAS ALEATORIAMENTE
            //SI NO LOS TENEMOS EN MEMORIA LO CARGAMOS DE LOS FICHEROS Y SI NO HAY DATOS DEVOLVEMOS ERROR

            //Comprobacion incidencias

            // Comprobacion Tecnicos
            boolean check1Ok = false;
            if (DepartamentoTecnico.getLista().size() == 0) {
                CargarFicheros_OOS.FicheroTecnicos();
                //Si la carga ha ido bien correcto
                if (DepartamentoTecnico.getLista().size() > 0) check1Ok = true;
            } else check1Ok = true;

            //Comprobacion Incidencias
            boolean check2Ok = false;
            if (IncidenciasReportadas.getLista().size() == 0) {
                CargarFicheros_OOS.FicheroIncidencias();
                //Si la carga ha ido bien correcto
                if (IncidenciasReportadas.getLista().size() > 0) check2Ok = true;
            } else check2Ok = true;


            //Si TENEMOS LOS DATOS MINIMOS PARA GENERAR TRABAJOS ALEATORIAMENTE, NOS PONEMOS A ELLO

            if (check1Ok && check2Ok) {

                //calculamos el número máximo de trabajos que se pueden atender en comparación con los
                // por el parametro nTRabajos
                //variables para el MAXIMO son  El numero de tecnicos y el numero de incidencias no cerradas
                //o el numero de técnicos por 2
                int capMax = DepartamentoTecnico.getLista().size()+2;
                nTrabajosMax = IncidenciasReportadas.getIncidenciasNoCerradasList().size();
                if (capMax< nTrabajosMax) nTrabajosMax=capMax;

                CargarFicheros_OOS.FicheroTrabajos();

                Random rnd = new Random(); //generador de aleatorios
                int maxHoras = 4; //el maximo de horas que un técnico puede dedicar a una incidencia en un día
                int incSelIdx;  //Id de incidencia aleatoria
                int tecSelIdx;  //Id de tecnico aleatorio

                if (IncidenciasReportadas.getIncidenciasNoCerradasList().size()==0)
                    System.out.println("No hay incidencias reportadas pendientes de atender.");
                else {
                    //A ver cuanto trabajamos hoy....(mínimo una incidencia)
                    if (nTrabajos < 0) nTrabajos= new Random().nextInt(nTrabajosMax-1)+1;

                    System.out.format("\nAtendiendo %d incidencias\n", nTrabajos);

                    for (int i = 0; i < nTrabajos; i++) {

                        //Seleccionamos una incidencia no cerrada sobre la que trabajar
                        incSelIdx = rnd.nextInt(IncidenciasReportadas.getIncidenciasNoCerradasList().size()-1)+1;
                        tecSelIdx = rnd.nextInt(DepartamentoTecnico.getLista().size()-1)+1; // Tecnico que atiende la incidencia.
                        Incidencia incidenciaSel = IncidenciasReportadas.getIncidenciasNoCerradasList().get(incSelIdx);

                        //Seleccionamos el tecnico que trabajará la incidencia
                        Tecnico tecnicoSel = DepartamentoTecnico.getLista().get(tecSelIdx);

                        //GENERAMOS EL TRABAJO CON LOS DATOS ALEATORIOS Y LA GUARDAMOS EN LA LISTA DE TRABAJOS

                        //Preguntamos el numero de horas trabajadas en la incidencia
                        int horas = rnd.nextInt(maxHoras - 1) + 1;

                        //Montamos el trabajo
                        Trabajo nuevoTrabajo = new Trabajo(
                                i+1,
                                incidenciaSel.getId(),
                                tecnicoSel.getId(),
                                horas
                        );

                        //actualizamos la incidencia

                        //sumamos las horas
                        incidenciaSel.setHoras(incidenciaSel.getHoras() + nuevoTrabajo.getHoras());

                        //Preguntamos si el tecnico a terminado la incidencia y
                        // si esta resuelta actualizamos la incidencia y
                        //la añadimos a la lista de incidencias cerradas
                        boolean resuelta = rnd.nextBoolean();
                        if (resuelta) {
                            incidenciaSel.setResuelta(true);
                            incidenciaSel.setIdTecnicoCierre(tecnicoSel.getId());
                            IncidenciasReportadas.getIncidenciasCerradasList().add(incidenciaSel);
                            IncidenciasReportadas.getIncidenciasNoCerradasList().remove(incidenciaSel);
                        }

                        //Guardamos la incidencia ya modificada en memoria
                        IncidenciasReportadas.getMap().put(incidenciaSel.getId(), incidenciaSel);


                        //Mostramos los datos del trabajo en pantalla
                        System.out.format("%s\n\tTecnico: %s\n\tHoras: %d\n",
                                incidenciaSel.isResuelta() ? incidenciaSel + " * * *" : incidenciaSel,
                                tecnicoSel,
                                horas);

                        //Añadimos el trabajo a la lista de nuevosTrabajos
                        TrabajosRealizados.add(nuevoTrabajo);

                    }
                    //FIN DE LOS TRABAJOS
                    EscribirFicheros_OOS.FicheroTrabajos();
                    EscribirFicheros_OOS.FicheroIncidencias();
                    EscribirFicheros_OOS.FicheroTrabajos();
                }

            } else System.out.println("Imposible atender incidencias, problemas con los ficheros o ausencia de datos necsarios en los mismos");

        }

    }

    /**
     * Clase que resetea o restaura un conjunto de datos de prueba en los ficheros
     * para el uso del proyecto.
     * Los ficheros serán borrados.
     */
    private static class DatosParaPruebas {

        public static void main(String[] args) {

            // DEPARTAMENTO TECNICO
             {
                System.out.println("Eliminando lista actual de tecnicos...");
                DepartamentoTecnico.getMap().clear();

                System.out.println("Creacion de Tecnicos...");
                String[] nombres = {"Juanjo", "Karmen", "Blanca", "Dominique", "Mikel", "Gorka", "Nuria"};
                TechSpecialization[] especialidades = {
                        TechSpecialization.General,
                        TechSpecialization.Programacion,
                        TechSpecialization.Programacion,
                        TechSpecialization.Redes,
                        TechSpecialization.Comunicaciones,
                        TechSpecialization.Sistemas,
                        TechSpecialization.General
                };
                String[] ciudades = {"Bilbao", "Vitoria", "Bilbao", "Vitoria", "Bilbao", "Donosti", "Donosti"};
                String[] categorias = {"Senior", "Senior", "Senior", "Junior", "Junior", "Junior", "Senior"};
                System.out.println("Listado de tecnicos añadidos");
                for (int i = 0; i < nombres.length; i++) {
                    Tecnico t = new Tecnico(
                            i+1,
                            nombres[i],
                            especialidades[i],
                            ciudades[i],
                            categorias[i]);
                    DepartamentoTecnico.add(t);
                    System.out.println(t);
                }
                System.out.println("Guardando fichero ...");
                EscribirFicheros_OOS.FicheroTecnicos();
                System.out.println();
            }

            // FICHERO TIPOS DE INCIDENCIA
            {
                System.out.println("Eliminando lista actual de Tipos de incidencias...");
                TiposDeIncidencias.getLista().clear();

                System.out.println("Creacion de Tipos de Incidencia...");
                String[] tipos = {"Problema Hardware", "Error de acceso", "Problema de SW", "Error página Web",
                        "Error certificados", "Fallo metodología", "Problema red", "Usuario problematico"};
                for (String tipo : tipos) {
                    TiposDeIncidencias.add(tipo);
                    System.out.println(tipo);
                }
                System.out.println("Guardando fichero ...");
                EscribirFicheros_OOS.FicheroTipoIncidencias();
                System.out.println();
            }

            //  AREAS DE EMPRESA
            {
                System.out.println("Eliminando lista actual de Areas de Empresa...");
                AreasEmpresa.getMap().clear();

                System.out.println("Creacion de Areas de Empresa...");
                String[] areas = {"Ingeniería", "Taller", "Compras", "RRHH", "Administración", "TICS", "Montaje"};
                int[] numeroPersonas = {30, 20, 8, 4, 5, 3, 10};
                for (int i = 0; i < areas.length; i++) {
                    //AreasEmpresa.add(new Area(i, areas[i], numeroPersonas[i]));
                    AreasEmpresa.add(new Area(i+1, areas[i], numeroPersonas[i]));
                    System.out.println(areas[i]);
                }
                System.out.println("Guardando fichero ...");
                EscribirFicheros_OOS.FicheroAreas();
            }
        }
    }
}
