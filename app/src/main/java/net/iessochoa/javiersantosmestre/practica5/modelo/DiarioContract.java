package net.iessochoa.javiersantosmestre.practica5.modelo;

import android.provider.BaseColumns;
/**
 * Esta clase nos permitira mantener aislado la base de datos del código
 * Es conveniente crearla y utilizar sus propiedades en vez de los nombres
 * directos de la base de datos en el código
 */


public class DiarioContract {
    public static class DiaDiarioEntries{
        public static final String TABLE_NAME="diadiario";
        public static final String ID= BaseColumns._ID;//esta columna es necesaria para Android
        public static final String FECHA="fecha";
        public static final String VALORACION_DIA="valoraciondia";
        public static final String RESUMEN="resumen";
        public static final String CONTENIDO="contenido";
        public static final String FOTO_URI="fotouri";
        public static final String LATITUD="latitud";
        public static final String LONGITUD="longitud";
    }
}
