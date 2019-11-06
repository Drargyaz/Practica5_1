package net.iessochoa.javiersantosmestre.practica5.modelo;
import android.content.Context;
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
    private final static String DB_NOMBRE = "diario.db";

    private static final String DATABASE_NAME = "diario.db";
    //creamos las sentencias que nos serán útiles en la clase. Muchas de ellas parametrizadas para crear la tabla
    private static final String CREATE_TABLE = "CREATE TABLE if not exists " + DiaDiarioEntries.TABLE_NAME + " ("
            + DiaDiarioEntries.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DiaDiarioEntries.FECHA + " TEXT UNIQUE NOT NULL,"
            + DiaDiarioEntries.VALORACION_DIA + " INTEGER NOT NULL,"
            + DiaDiarioEntries.RESUMEN + "TEXT NOT NULL,"
            + DiaDiarioEntries.CONTENIDO + "TEXT NOT NULL,"
            + DiaDiarioEntries.FOTO_URI + " TEXT UNIQUE"
            + ")";

    //Constante que se utiliza en el caso de querer borrar la tabla
    private final static String SQL_DELETE_TABLE= "DROP TABLE IF EXISTS "+DiaDiarioEntries.TABLE_NAME;

    //Constante que se utiliza a la hora de querer insertar datos en la tabla
    private static final String SQL_INSERT = "INSERT INTO " + DiaDiarioEntries.TABLE_NAME +
            " VALUES(" + DiaDiarioEntries.FECHA +
            "," + DiaDiarioEntries.VALORACION_DIA +
            "," + DiaDiarioEntries.RESUMEN +
            "," + DiaDiarioEntries.CONTENIDO +
            ") VALUES (?,?,?,?)";

    //Nos permitira abrir la base de datos
    private DBHelper dbH;
    private SQLiteDatabase db;

    public DiarioDB(Context context) {
        dbH = new DBHelper(context);
    }

    /**
     * Abre la base de datos
     */
    public void open() throws SQLiteException {
            db = dbH.getWritableDatabase();
    }

    public void close() throws SQLiteException {
        if (db.isOpen()) {
            db.close();
        }
    }

    /**
     * Con esta clase le diremos a android que cree la base de datos o que
     * tiene que hacer cuando se modifica la version de la base de datos
     */
    private class DBHelper extends SQLiteOpenHelper {


        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DB_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            db.execSQL(SQL_DELETE_TABLE);
            db.execSQL(CREATE_TABLE);
        }
    }

    public void borraDia(DiaDiario dia)  throws SQLiteException, SQLiteConstraintException {
        db.delete(DiaDiarioEntries.TABLE_NAME, DiaDiarioEntries.FECHA + "= ?", new String[]{fechaToFechaDB(dia.getFecha())});
    }

    public static Date fechaBDtoFecha(String f){
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha=null;
        try {
            fecha= formatoDelTexto.parse(f);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return fecha;
    }

    public static String fechaToFechaDB(Date fecha){
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(fecha);
    }
}
