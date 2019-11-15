package net.iessochoa.javiersantosmestre.practica5.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import net.iessochoa.javiersantosmestre.practica5.R;
import net.iessochoa.javiersantosmestre.practica5.modelo.DiaDiario;
import net.iessochoa.javiersantosmestre.practica5.modelo.DiarioContract;
import net.iessochoa.javiersantosmestre.practica5.modelo.DiarioDB;

public class MainActivity extends AppCompatActivity {

    private static final int OPTION_REQUEST_CREAR_DIA = 0;
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
        this.mostrarDatos(DiarioContract.DiaDiarioEntries.FECHA);

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
                //Creamos un día, en este caso vacia porque no tiene los valores asignados.
                DiaDiario dia=new DiaDiario();
                //Creamos el intent para llamar a la actividad
                Intent intent = new Intent(MainActivity.this, EdicionDiaActivity.class);
                //Enviamos el nuevo día con los datos vacios
                intent.putExtra(EdicionDiaActivity.EXTRA_DIA, dia);
                //Llamamos a la actividad a la espera de recibir el resultado
                //indicando el código de llamada (OPTION_REQUEST_CREAR_DIA)
                startActivityForResult(intent, OPTION_REQUEST_CREAR_DIA);
                return true;
            case R.id.opcionBorrar:
                //TODO implement
                return true;
            case R.id.opcionOrdenar:
                dialogoOrdenar();
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

    private void mostrarDatos(String ordenarPor) {
        Cursor c = diarioDB.obtenDiario(ordenarPor);
        DiaDiario dia;
        tvMostrar.setText("");//limpiamos el campo de texto
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                dia=DiarioDB.cursorADiaDiario(c);
                //podéis sobrecargar toString en DiaDiario para mostrar los datos
                tvMostrar.append(dia.toString()+"\n");
            } while(c.moveToNext());
        }
    }

    @Override
    //Método que se ejecuta para comprobar el resultado actual de la actividad
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Comprobamos si el resultado de la segunda actividad no es "RESULT_CANCELED" (pulsado el botón de Cancelar).
        if (resultCode == RESULT_OK && requestCode == OPTION_REQUEST_CREAR_DIA) {
            // Si no lo es, almacenamos el resultado de EdicionDiaActivity, que es el dia creado.
            DiaDiario dia = data.getParcelableExtra(EdicionDiaActivity.EXTRA_DIA);
            // Añadimos el dia a la base de datos y lo mostramos en el TextView
            Toast.makeText(this,data.getParcelableExtra(EdicionDiaActivity.EXTRA_DIA)+"",Toast.LENGTH_SHORT).show();
            //diarioDB.anyadeActualizaDia(dia);
        }
    }

    //Dialogo que muestra una lista y en función del item elegido ordena de una forma o de otra.
    public void dialogoOrdenar() {
        final CharSequence[] items = { getResources().getString(R.string.opcionOrdenarFecha), getResources().getString(R.string.opcionOrdenarValoracion), getResources().getString(R.string.opcionOrdenarResumen)};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.tituloDialogoOrdenar));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                mostrarDatos(items[item].toString());
            }
        }).show();
    }


    @Override
    //Al destruir la actividad, cierra la base de datos
    protected void onDestroy() {
        super.onDestroy();
        diarioDB.close();
    }
}
