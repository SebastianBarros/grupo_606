package com.example.barros_costa_tp2_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //iniciar los botones y los text field. Crear el user si todo está OK y validar la contraseña
        //la petición rest manda sólo mail y pw, lo demás va en blanco.
    }
}
