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

    private static final String PROXIMITY = "PROXIMITY";
    private static final String LIGHT = "LIGHT";
    private static final int LENGTH = 3;
    // variables de managment de sensores
    private SensorManager sensorManagerLux;
    private SensorManager sensorManagerProximity;
    //objeto sensor donde seran registrados cada sensor
    private Sensor lux;
    private Sensor proximity;
    private float lightValues[] = new float[LENGTH];
    private float proximityValues[] = new float[LENGTH];


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

        protected void onPause()
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
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximity = event.values[0];
            Log.i("valor proximidad:", String.valueOf(proximity) + event.timestamp);


            //la primera vez que se ejecute (y no este el sharedPreferences, el array va a estar vacio. Vamos a guardar los primeros 3 valores.
            // Dsp los vamos actualizando a medida que cambien. El 3 es un valor aleatorio que elegi. Si querés mostrar 10 valores en pantalla, entonces acá poner un 10.
            if(proximityValues.length < LENGTH) {
                proximityValues[proximityValues.length] = proximity;
            }

            if(proximity != proximityValues[0]){
                changeValues(proximity, PROXIMITY);
            }
            //acá llamarías al método para mostrar en pantalla, en el vector tenés los valores

            textViewProximity.setText(Float.toString(proximity));  //seteo el ultimo valor que cambio de proximidad

        }

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lux = event.values[0];
            Log.i("valor luz:", String.valueOf(lux));


            //la primera vez que se ejecute (y no este el sharedPreferences, el array va a estar vacio. Vamos a guardar los primeros 3 valores.
            // Dsp los vamos actualizando a medida que cambien. El 3 es un valor aleatorio que elegi. Si querés mostrar 10 valores en pantalla, entonces acá poner un 10.
            if(proximityValues.length < LENGTH) {
                proximityValues[proximityValues.length] = lux;
            }

            if(lux != lightValues[0] && Math.abs(event.values[0] - lightValues[0]) > 50) {
                changeValues(lux, LIGHT);
            }

            //acá llamarías al método para mostrar en pantalla, en el vector tenés los valores
            textViewLux.setText(Float.toString(lux)); // seteo el ultimo valor que cambio el nivel de luz
        }

    }

    private void changeValues(float value, String type) {
        if(PROXIMITY.equals(type)) {
            for(int i = proximityValues.length - 1; i > 0; i--) {
                proximityValues[i] = proximityValues[i-1];
            }
            proximityValues[0] = value;
        } else {
            for(int i = lightValues.length - 1; i > 0; i--) {
                lightValues[i] = lightValues[i-1];
            }
            lightValues[0] = value;
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
