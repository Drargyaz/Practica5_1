package net.iessochoa.javiersantosmestre.practica5.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.iessochoa.javiersantosmestre.practica5.R;
import net.iessochoa.javiersantosmestre.practica5.modelo.DiaDiario;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EdicionDiaActivity extends AppCompatActivity {
    //Constante que nos permitirá pasar el día con sus respectivos datos desde la actividad EdicionDiaActivity para insetarlo en la base de datos
    public static final String EXTRA_DIA = "net.iessochoa.javiersantosmestre.practica5.dia" ;

    //Declaracion de las variables que vamos a usar y modificar, en este caso un Spinner para la valoracion,
    // un editText multilinea para el contenido del dia, otro editText para el resumen del dia, un objeto DiaDiario para pasarlo
    //un TextView para la fecha y un boton para guardar los datos dentro de un objeto DiaDiario

    TextView tvFecha;
    Button btFecha;
    FloatingActionButton btGuardar;
    EditText etContenido;
    EditText etResumen;
    Spinner spValoracion;
    DiaDiario dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_dia);
        tvFecha=findViewById(R.id.tvFecha);
        etContenido=findViewById(R.id.etContenido);
        etResumen=findViewById(R.id.etResumen);
        spValoracion=findViewById(R.id.spValoracion);
        btGuardar=findViewById(R.id.btGuardar);
        btFecha=findViewById(R.id.btFecha);
        //Mostramos por defecto en el tvFecha la fecha actual
        tvFecha.setText(fechaATexto(Calendar.getInstance().getTime()));
        //Asigno un adaptador al Spinner de Valoracion para asi cargar del Array de Valoracion que tenemos en el XML,
        // sus valores en los items del Spinner y que por defecto el valor del spValoracion sea 5
        spValoracion.setAdapter(new ArrayAdapter<String>(EdicionDiaActivity.this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.valoraciones)));
        spValoracion.setSelection(5);

        //Metodo que se ejecuta al clickar en el boton de fecha
        btFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoFecha();
            }
        });
        //Metodo que se ejecuta al pulsar el boton guardar
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si hay algun campo vacio, te saltará el dialogo que hay campos vacios.
                if (etResumen.getText().toString().isEmpty() || etContenido.getText().toString().isEmpty()){
                    dialogoHayCamposVacios();
                }else{
                    Date fecha = textoAFecha(tvFecha.getText().toString());
                    int valoracion = Integer.parseInt(spValoracion.getItemAtPosition(spValoracion.getSelectedItemPosition()).toString());
                    String resumen = etResumen.getText().toString();
                    String contenido = etContenido.getText().toString();
                    dia = new DiaDiario(fecha,valoracion,resumen,contenido);
                    //Almacenamos el resultado en el Intent que llamó la actividad
                    Intent i = getIntent();
                    i.putExtra(EXTRA_DIA, dia);
                    //Indicamos que se ha pulsado aceptar y enviamos el Intent
                    setResult(RESULT_OK,i);
                    finish();
                }
            }
        });
    }

    private void dialogoFecha(){
        //Toast.makeText(this,textoAFecha(tvFecha.getText().toString())+"",Toast.LENGTH_SHORT).show();
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog dialogo = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        //Actualizamos el TextView con la fecha seleccionada
                        tvFecha.setText(fechaATexto(calendar.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        //esto último es el dia a mostrar
        dialogo.show();

    }

    //Metodo que nos muestra un AlertDialog (dialogo) si al guardar hay algun campo vacio en la actividad
    private void dialogoHayCamposVacios() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        //Titulo del dialogo
        dialogo.setTitle(getResources().getString(R.string.tituloDialogoCamposVacios));
        //Mensaje que aparece en el dialogo
        dialogo.setMessage(getResources().getString(R.string.mensajeDialogoCamposVacios));
        // Creación boton ok y lo que ocurriria al ser clickado
        dialogo.setPositiveButton(android.R.string.yes
                , new DialogInterface.OnClickListener() {
                    @Override
                    //Metodo que al clickar en el botón de ok, cancela el dialogo y lo cierra.
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        //Muestra el dialogo
        dialogo.show();
    }

    private String fechaATexto(Date fecha){
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
    }

    private Date textoAFecha(String fechaAPasar){
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(fechaAPasar);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
