package com.example.barros_costa_tp2_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        LinearLayout lightLayout = findViewById(R.id.linearLayoutLux);
        LinearLayout  proximityLayout = findViewById(R.id.linearLayoutProximity);
        Button saveLightData = (Button) findViewById(R.id.buttonRegisterLuxSensor);
        Button saveProximityData = (Button) findViewById(R.id.buttonRegisterProximitySensor);
        Button goBackToLogin = (Button) findViewById(R.id.buttonBack);

        saveLightData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveLightData();
            }
        });

        saveProximityData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveProximityData();
            }
        });

        goBackToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goBackToLogin();
            }
        });

        //disparar 2 hilos que manejen el sensor de luz y de proximidad
        }

    private void goBackToLogin() {
        MenuActivity.this.startActivity(new Intent(MenuActivity.this, MainActivity.class));
    }

    private void saveProximityData() {
        //do stuff
    }

    private void saveLightData() {
        //do stuff
    }


    //obtener el nombre del user
    //botón logout
    //botón registrar -> creás un hilo aparte donde levantás los datos de la pantalla y hacés 2 post con diferente type y datos.
    //actualizar las grillas con los datos de los sensores

}
