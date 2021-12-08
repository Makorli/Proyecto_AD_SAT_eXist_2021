package com.company.Controlador;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class DBController {

    private static String driver = "org.exist.xmldb.DatabaseImpl"; //Driver para eXist
    private static final String URI = "xmldb:exist://localhost:8081/exist/xmlrpc/db"; //URI colección
    private static final String usu = null; //Usuario
    private static final String usuPwd = null; //Clave
    private static boolean initialized = false;
    private static Collection defaultCollection = null;

    static Database database = null;

    public static void init() {
        if (!isInitialized()) {
            System.out.print("Inicializando base de datos....");
            try {
                System.out.print("Loading Driver..");
                Class myDriver = Class.forName(driver); //Cargar del driver
                System.out.print("..OK  Generating Instance..");
                database = (Database) myDriver.getDeclaredConstructor().newInstance();//Instancia de la BD
                System.out.print("..OK \nRegistering Database...");
                DatabaseManager.registerDatabase(database); //Registro del driver
                initialized = true;
            } catch (XMLDBException e) {
                System.out.println("Error al inicializar la BD eXist.");
                //e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Error en el driver.");
                e.printStackTrace();
            } catch (InstantiationException | InvocationTargetException e) {
                System.out.println("Error al instanciar la BD.");
                //e.printStackTrace();
            } catch (NoSuchMethodException e) {
                System.out.println("Conexión: Operacion no reconocida");
            } catch (IllegalAccessException e) {
                System.out.println("Acceso no permitido a la BD.");
            }
            System.out.println("..Done!");
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static Collection getDefaultCollection() {
        return defaultCollection;
    }

    public static Collection getCollectionFromDB(String colName) {
        init();
        String myURI = String.format("%s/%s", URI, colName);
        try {
            return DatabaseManager.getCollection(URI, usu, usuPwd);
        } catch (XMLDBException e) {
            System.out.println("Error en la conexion XMLDB.");
        }
        return null;
    }

    public static boolean existCollection(String colName) {
        init();
        String myURI = String.format("%s/%s", URI, colName);
        Collection col = getCollectionFromDB(colName);
        return col != null;
    }

    public static boolean setDeFaultCollection(String colName) {
        init();
        Collection tempcol = getCollectionFromDB(colName);
        if (tempcol == null) return false;
        defaultCollection = tempcol;
        return true;
    }

    public static void close(Collection col) {
        try {
            if (col.isOpen()) col.close();
        } catch (XMLDBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga el fichero especificado con el nombre pasado por parametro en la base de datos
     * en una coleccion ya existente
     *
     * @param file fichero a cargar
     * @param name del fichero dentro de la colección de la base de datos
     */
    public static void loadIntoDB(File file, String name, Collection collection) {
        String nombre = name.toLowerCase().trim();
        assert (!nombre.equals(""));
        //Collection collection = getDefaultCollection();
        if (collection != null) {
            try {
                System.out.print("Conecta");
                // Inicializamos el recurso
                XMLResource res = null;
                res = (XMLResource) collection.createResource(
                        nombre,
                        "XMLResource");
                //Si existe el fichero
                if (file.exists()) {
                    res.setContent(file); // Fijamos como contenido ese archivo .xml elegido
                    collection.storeResource(res); // lo añadimos a la colección
                    System.out.format("Fichero: %s cargado correctamente como %s",
                            file.getName(),
                            nombre);
                } else {
                    System.out.format("El fichero %s no existe", file.getName());
                }
                /* Listamos la colección para ver que en efecto se ha añadido
                for (String colRe : collection.listResources())
                    System.out.println(colRe);
                 */
                close(collection);
            } catch (XMLDBException e) {
                System.out.format("Error en la carga del fichero en %S", nombre);
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
    }
    public static void loadIntoDB(File file, String name) {
        loadIntoDB(file,name,getDefaultCollection());
    }

    public static void main(String[] args) {
        File myfile = new File("src/main/java/com/company/DataXML/Tecnicos.xml");

        loadIntoDB(myfile, "Mistecnicos.xml");
    }

    public static boolean createCollection(String colName) {
        init();
        if (!existCollection(colName)) {
            Collection col = null;
            try {
                CollectionManagementService mgtService = (CollectionManagementService) col.getService("CollectionManagementService", "1.0");
                mgtService.createCollection(colName);
                return true;
            } catch (XMLDBException e) {
                System.out.println("No se ha podido crear la coleccion " + colName.toUpperCase());
            }
        }
        return false;
    }

    public static boolean addFilesToCollection(File file, String colName) {

        //VERIFICAMOS Y DEFINIMOS LA COLECCION
        Collection mycollection = null;
        if (colName == null || colName.equals("")) {
            if (defaultCollection == null) {
                System.out.println("Nose ha especificado ninguna colleción válida");
                return false;
            } else mycollection = defaultCollection;
        } else {
            String myURI = String.format("%s/%s", URI, colName);
            try {
                mycollection = DatabaseManager.getCollection(myURI, usu, usuPwd);
            } catch (XMLDBException e) {
                System.out.println("Error en la conexión a la coleccion ");
                return false;
            }
        }

        //VERIFICAMOS Y AÑADIMOS EL FICHERO
        if (!file.canRead()) {
            System.out.println("No hay acceso al fichero " + file.getPath());
        } else {
            Resource res;
            try {
                res = mycollection.createResource(file.getName(),"XMLResource");
                res.setContent(file);
                mycollection.storeResource(res);
                System.out.printf("Fichero %s agregado con exito%n", file.getName());
            } catch (XMLDBException e) {
                System.out.printf("Error en la agregación del fichero ", file.getName());
                return false;
            } catch (NullPointerException n){
                System.out.println("No se encuentra fichero "+file.getPath());
            }
        }
        return true;
    }

}



