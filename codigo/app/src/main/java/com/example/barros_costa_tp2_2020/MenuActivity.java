package com.example.barros_costa_tp2_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MenuActivity extends AppCompatActivity implements SensorEventListener {

    // variables de managment de sensores
    private SensorManager sensorManagerLux;
    private SensorManager sensorManagerProximity;
    //objeto sensor donde seran registrados cada sensor
    private Sensor lux;
    private Sensor proximity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        LinearLayout lightLayout = findViewById(R.id.linearLayoutLux);
        LinearLayout  proximityLayout = findViewById(R.id.linearLayoutProximity);
        Button saveLightData = (Button) findViewById(R.id.buttonRegisterLuxSensor);
        Button saveProximityData = (Button) findViewById(R.id.buttonRegisterProximitySensor);
        Button goBackToLogin = (Button) findViewById(R.id.buttonBack);

        sensorManagerLux = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lux = sensorManagerLux.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManagerProximity = (SensorManager) getSystemService((Context.SENSOR_SERVICE));
        proximity = sensorManagerProximity.getDefaultSensor(Sensor.TYPE_PROXIMITY);


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

        protected void onResume()
        {
            super.onResume();
            //los sensores no haran nada hasta que la actividad este en primer plano.
            // aca se registran, no olvidar de-registrarlos porque en si se la actividad va onPaused y no lo haces siguen corriendo.
            //tengo que ver como meter esto en dos hilos distintos. por ahora no llego a colgarse pero deberia ir en cada hilo tengo entendido
            sensorManagerLux.registerListener(this,lux,SensorManager.SENSOR_DELAY_NORMAL);
            sensorManagerProximity.registerListener(this,proximity,SensorManager.SENSOR_DELAY_NORMAL);

        }

        protected void onPaused()
        {
            super.onPause();
            sensorManagerLux.unregisterListener(this);
            sensorManagerProximity.unregisterListener(this);
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

    @Override
    public void onSensorChanged(SensorEvent event) {

        // referencio los textView del layout.
        // esto solo muestra en pantalla
        TextView textViewLux = (TextView) findViewById(R.id.textViewLux);
        TextView textViewProximity = (TextView) findViewById(R.id.textViewProximity);
        float proximity,lux; // variables tipo float para almacenar los valores captados por el sensor

        //Esta lógica es para diferenciar los eventos del sensor .getType me da el tipo de sensor que mensajeo a event.sensor
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            proximity = event.values[0];
            Log.i("valor proximidad:", String.valueOf(proximity));
            textViewProximity.setText(Float.toString(proximity));  //seteo el ultimo valor que cambio de proximidad
        }
        if (event.sensor.getType() == Sensor.TYPE_LIGHT)
        {
            lux = event.values[0];
            Log.i("valor luz:", String.valueOf(lux));
            textViewLux.setText(Float.toString(lux)); // seteo el ultimo valor que cambio el nivel de luz
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        //No se que poronga deberiamos poner aca.

    }


    //obtener el nombre del user
    //botón logout
    //botón registrar -> creás un hilo aparte donde levantás los datos de la pantalla y hacés 2 post con diferente type y datos.
    //actualizar las grillas con los datos de los sensores

}
