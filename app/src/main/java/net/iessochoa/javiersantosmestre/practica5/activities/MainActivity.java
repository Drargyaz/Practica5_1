package net.iessochoa.javiersantosmestre.practica5.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.iessochoa.javiersantosmestre.practica5.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

}
