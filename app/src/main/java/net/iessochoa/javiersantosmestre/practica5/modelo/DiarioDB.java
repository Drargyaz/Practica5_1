package net.iessochoa.javiersantosmestre.practica5.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import net.iessochoa.javiersantosmestre.practica5.modelo.DiarioContract.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DiarioDB {

    //Constantes que almacenan la versión de la Base de Datos y el nombre del archivo
    private final static int DB_VERSION = 1;
    private static final String DATABASE_NAME = "diario.db";

    //creamos las sentencias que nos serán útiles en la clase. Muchas de ellas parametrizadas para crear la tabla
    private static final String CREATE_TABLE = "CREATE TABLE if not exists " + DiaDiarioEntries.TABLE_NAME + " ("
            + DiaDiarioEntries.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DiaDiarioEntries.FECHA + " TEXT UNIQUE NOT NULL,"
            + DiaDiarioEntries.VALORACION_DIA + " INTEGER NOT NULL,"
            + DiaDiarioEntries.RESUMEN + " TEXT NOT NULL,"
            + DiaDiarioEntries.CONTENIDO + " TEXT NOT NULL,"
            + DiaDiarioEntries.FOTO_URI + " TEXT"
            + ")";

    //Constante que se utiliza en el caso de querer borrar la tabla
    private final static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + DiaDiarioEntries.TABLE_NAME;

    //Nos permitira abrir la base de datos
    private DBHelper dbH;
    private SQLiteDatabase db;

    public DiarioDB(Context context) {
        dbH = new DBHelper(context);
    }


    //Metodo que abre la base de datos sino esta abierta o si  no está inicializada
    public void open() throws SQLiteException {
        if (db == null || !db.isOpen()) {
            db = dbH.getWritableDatabase();
        }

    }

    //Metodo que cierra la base de datos si está abierta
    public void close() throws SQLiteException {
        if (db.isOpen()) {
            db.close();
        }
    }

    //Esta clase le dice a android que cree la base de datos o lo que tiene que hacer cuando se modifica su versión
    private class DBHelper extends SQLiteOpenHelper {


        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DB_VERSION);
        }


        @Override
        //Metodo que nos crea una tabla en la base de datos mediante la secuencia SQL pasada por la constante que tenemos CREATE_TABLE
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }

        @Override
        //Metodo que nos actualiza la base de datos borrando la tabla a actualizar y luego volviendola a crear actualizada
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }
    }

    //Metodo que borra un dia de la base de datos
    public void borraDia(DiaDiario dia) throws SQLiteException, SQLiteConstraintException {
        db.delete(DiaDiarioEntries.TABLE_NAME, DiaDiarioEntries.FECHA + "= ?", new String[]{fechaToFechaDB(dia.getFecha())});
    }

    //Metodo que inserta un dia en la base de datos pasado como parametro
    public void anyadeActualizaDia(DiaDiario dia) {
        //Insertamos cada valor del dia en el ContentValues para asi luego insertar el dia en la tabla o actualizarlo
        ContentValues valores = new ContentValues();
        valores.put(DiaDiarioEntries.FECHA, fechaToFechaDB(dia.getFecha()));
        valores.put(DiaDiarioEntries.VALORACION_DIA, dia.getValoracionDia());
        valores.put(DiaDiarioEntries.RESUMEN, dia.getResumen());
        valores.put(DiaDiarioEntries.CONTENIDO, dia.getContenido());
        if (!dia.getFotoUri().isEmpty()) {
            valores.put(DiaDiarioEntries.FOTO_URI, dia.getFotoUri());
        }
        //Sentencia where que comprueba si el dia existe, si existe, lo actualiza.
        String where = DiaDiarioEntries.FECHA + "=?";
        String[] arg = new String[]{fechaToFechaDB(dia.getFecha())};
        //actualizamos si el dia existe en el caso de que no, lo insertamos.
        if(db.update(DiaDiarioEntries.TABLE_NAME, valores, where, arg)==0){
            //Usamos el insertOrThrow para en caso de tener problemas salte la excepcion correspondiente
            db.insertOrThrow(DiaDiarioEntries.TABLE_NAME, null, valores);
        }
    }

    public Cursor obtenDiario(String ordenadoPor) {
        if (ordenadoPor != null) {
            if (ordenadoPor.equals("fecha")) {
                return db.query(DiaDiarioEntries.TABLE_NAME, null, null, null, null, null, DiaDiarioEntries.FECHA);
            } else if (ordenadoPor.equals("valoracion")) {
                return db.query(DiaDiarioEntries.TABLE_NAME, null, null, null, null, null, DiaDiarioEntries.VALORACION_DIA);
            } else {
                return db.query(DiaDiarioEntries.TABLE_NAME, null, null, null, null, null, DiaDiarioEntries.RESUMEN);
            }
        }
        return null;
    }

    //Metodo que nos devuelve la valoración media de la vida
    public int valoraVida() {
        Cursor c = db.rawQuery("SELECT AVG(" + DiaDiarioEntries.VALORACION_DIA + ") FROM " + DiaDiarioEntries.TABLE_NAME, null);
        int media = 0;
        if (c != null) {
            //Empieza el cursor por el primer valor
            c.moveToFirst();
            //Mientras que el cursor tenga valores
            do {
                media = c.getInt(c.getColumnIndex(DiaDiarioEntries.VALORACION_DIA));
            } while (c.moveToNext());
        }
        c.close();
        return media;
    }

    //Metodo para pasar un string a un formato fecha
    public static Date fechaBDtoFecha(String f) {
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        try {
            fecha = formatoDelTexto.parse(f);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return fecha;
    }

    //Metodo para pasar una fecha a un formato String
    public static String fechaToFechaDB(Date fecha) {
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
    }

    //Metodo que devuelve un DiaDiario de la posición del cursor actual.
    public static DiaDiario cursorADiaDiario(Cursor c) {
        //obtenemos los valores de cada dato en el cursor en función de su indice: fecha,valoracionDia,resumen,foto
        String fecha = c.getString(c.getColumnIndex(DiaDiarioEntries.FECHA));
        Date fechaPasada = fechaBDtoFecha(fecha);
        int valoracionDia = c.getInt(c.getColumnIndex(DiaDiarioEntries.VALORACION_DIA));
        String resumen = c.getString(c.getColumnIndex(DiaDiarioEntries.RESUMEN));
        String contenido = c.getString(c.getColumnIndex(DiaDiarioEntries.CONTENIDO));
        String foto = c.getString(c.getColumnIndex(DiaDiarioEntries.FOTO_URI));

        //Devolvemos un diaDiario que tiene los datos que hay en el cursor.
        return new DiaDiario(fechaPasada, valoracionDia, resumen, contenido, foto);
    }

    public void cargarDatos() {
        DiaDiario[] diasPorDefecto = {new DiaDiario(fechaBDtoFecha("10/06/2010"), 5, "Selectividad", "asdjakdaldsa sadsad sadsadsad asdsadasda",""),
                new DiaDiario(fechaBDtoFecha("10/12/2015"), 3, "Examen ADA", "asdjakasdsaldjas kdsajd ksajdka sdsadaldsa sadsad sadsadsad asdsadasda","")};
        for (DiaDiario d : diasPorDefecto) {
            this.anyadeActualizaDia(d);
        }
    }
}
