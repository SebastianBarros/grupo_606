package com.example.barros_costa_tp2_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //disparar 2 hilos que manejen el sensor de luz y de proximidad
    }

    //obtener el nombre del user
    //botón logout
    //botón registrar -> creás un hilo aparte donde levantás los datos de la pantalla y hacés 2 post con diferente type y datos.
    //actualizar las grillas con los datos de los sensores

}
