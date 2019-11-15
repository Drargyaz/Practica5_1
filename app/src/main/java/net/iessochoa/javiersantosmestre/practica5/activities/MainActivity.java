package net.iessochoa.javiersantosmestre.practica5.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.iessochoa.javiersantosmestre.practica5.R;
import net.iessochoa.javiersantosmestre.practica5.modelo.DiaDiario;
import net.iessochoa.javiersantosmestre.practica5.modelo.DiarioDB;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static public String TAG_ERROR = "P5EjemploDB-Error:";

    //Creamos el ListView, el adaptador del ListView y la base de datos
    private DiarioDB diarioDB;
    //private ListView lvLista;
    private TextView tvMostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMostrar = findViewById(R.id.tvMostrar);
        try {
            diarioDB = new DiarioDB(this);
            diarioDB.open();
        } catch (android.database.sqlite.SQLiteException e) {
            mostrarMensajeError(e);
        }
        diarioDB.cargarDatos();
        this.iniciaDatosListaDias();

    }

    //Método para crear las opciones del menú
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Método que se ejecuta y en función del Item del menú seleccionado ejecuta una cosa u otra.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcionAnyadir:
                //TODO implement
                return true;
            case R.id.opcionBorrar:
                //TODO implement
                return true;
            case R.id.opcionOrdenar:
                //TODO implement
                return true;
            case R.id.opcionOrdenarFecha:
                //TODO implement
                return true;
            case R.id.opcionOrdenarValoracion:
                //TODO implement
                return true;
            case R.id.opcionOrdenarResumen:
                //TODO implement
                return true;
            case R.id.opcionValorVida:
                //TODO implement
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void mostrarMensajeError(Exception e) {
        Log.e(TAG_ERROR, e.getMessage());
        Toast.makeText(this, getString(R.string.error_leerBD) + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void iniciaDatosListaDias() {
        Cursor c = diarioDB.obtenDiario("fecha");
        DiaDiario dia;
        tvMostrar.setText("");//limpiamos el campo de texto
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                dia=DiarioDB.cursorADiaDiario(c);
                //podéis sobrecargar toString en DiaDiario para mostrar los datos
                tvMostrar.append("askdljsad"+dia.toString()+"\n");
            } while(c.moveToNext());
        }
    }



    @Override
    //Al destruir la actividad, cierra la base de datos
    protected void onDestroy() {
        super.onDestroy();
        diarioDB.close();
    }
}
