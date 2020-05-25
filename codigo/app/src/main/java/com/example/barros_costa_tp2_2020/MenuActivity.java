package com.example.barros_costa_tp2_2020;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class MenuActivity extends AppCompatActivity implements SensorEventListener {

    private static final String URI_EVENT = "http://so-unlam.net.ar/api/api/event";
    private static final String EVENT_ACTION = "com.example.barros_costa_tp2_2020.intent.action.RESPONSE_EVENT";



public class MenuActivity extends AppCompatActivity implements SensorEventListener {

    private static final String PROXIMITY = "PROXIMITY";
    private static final String LIGHT = "LIGHT";
    private static final int MAX_LENGTH = 3;

    // variables de managment de sensores
    private SensorManager sensorManagerLux;
    private SensorManager sensorManagerProximity;
    //objeto sensor donde seran registrados cada sensor
    private Sensor lux;
    private Sensor proximity;

    private Intent intentReceived;
    private Bundle extrasReceived;
    private String token;
    JSONObject jsonObject;
    TextView textViewLux;
    TextView textViewProximity;
    private float proximityValue;
    private float luxValue;
    private ArrayList<Float> stackluxValue = new ArrayList<Float>();
    ReceptorServiceEvent receiver;// variables tipo float para almacenar los valores captados por el sensor


    private List<Float> lightValues = new ArrayList<>();
    private List<Float> proximityValues = new ArrayList<>();


    //esto puede que corresponda a otra clase
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        LinearLayout lightLayout = findViewById(R.id.linearLayoutLux);
        LinearLayout  proximityLayout = findViewById(R.id.linearLayoutProximity);
        Button saveLightData = findViewById(R.id.buttonRegisterLuxSensor);
        Button saveProximityData =  findViewById(R.id.buttonRegisterProximitySensor);
        Button goBackToLogin = findViewById(R.id.buttonBack);


        sensorManagerLux = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lux = sensorManagerLux.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManagerProximity = (SensorManager) getSystemService((Context.SENSOR_SERVICE));
        proximity = sensorManagerProximity.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        intentReceived = getIntent();
        extrasReceived = intentReceived.getExtras();
        token = extrasReceived.getString("token");
        textViewLux = findViewById(R.id.textViewLux1);
        textViewProximity = findViewById(R.id.textViewProximity);

        saveLightData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    saveLightData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        saveProximityData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    saveProximityData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

            //Inicias el sharedPref y creas un editor
            sharedPref = MenuActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            editor = sharedPref.edit();
            //Para levantar los datos usas getFloat. El primer parametro es la key (0 es el valor mas nuevo, 2 el mas viejo) y el 2do parametro es x mi no encuentra nada, devuelve ese nro
            Log.i("TEST", String.valueOf(sharedPref.getFloat("PROXIMITY0", 404)));
            Log.i("TEST", String.valueOf(sharedPref.getFloat("PROXIMITY1", 404)));
            Log.i("TEST", String.valueOf(sharedPref.getFloat("PROXIMITY2", 404)));
            Log.i("TEST", String.valueOf(sharedPref.getFloat("LIGHT0", 404)));
            Log.i("TEST", String.valueOf(sharedPref.getFloat("LIGHT1", 404)));
            Log.i("TEST", String.valueOf(sharedPref.getFloat("LIGHT2", 404)));

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

    private void saveProximityData() throws JSONException {
        jsonObject = new JSONObject();
        jsonObject.put("env","TEST");
        jsonObject.put("type_events","Proximity Sensor");
        jsonObject.put("state","ACTIVO");
        jsonObject.put("description",String.valueOf(proximityValue));

        Intent intentEvent = new Intent(MenuActivity.this, ServicePostEvent.class);
        intentEvent.putExtra("uri",URI_EVENT);
        intentEvent.putExtra("token",token);
        intentEvent.putExtra("jsondata",jsonObject.toString());
        intentEvent.putExtra("action",EVENT_ACTION);
        configureBroadCastReceiver();
        startService(intentEvent);
    }

    private void configureBroadCastReceiver() {
      receiver = new ReceptorServiceEvent();
        IntentFilter filterRegister =  new IntentFilter(EVENT_ACTION);
        filterRegister.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filterRegister);
    }

    private void saveLightData() throws JSONException {
        jsonObject = new JSONObject();
        jsonObject.put("env","TEST");
        jsonObject.put("type_events","Lux Sensor");
        jsonObject.put("state","ACTIVO");
        jsonObject.put("description",String.valueOf(luxValue));

        Intent intentEvent = new Intent(MenuActivity.this, ServicePostEvent.class);
        intentEvent.putExtra("uri",URI_EVENT);
        intentEvent.putExtra("token",token);
        intentEvent.putExtra("jsondata",jsonObject.toString());
        intentEvent.putExtra("action",EVENT_ACTION);
        configureBroadCastReceiver();
        startService(intentEvent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // referencio los textView del layout.
        // esto solo muestra en pantalla



        //Esta lógica es para diferenciar los eventos del sensor .getType me da el tipo de sensor que mensajeo a event.sensor
<<<<<<< HEAD
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            proximityValue = event.values[0];
            Log.i("valor proximidad:", String.valueOf(proximityValue));
            textViewProximity.setText(Float.toString(proximityValue));  //seteo el ultimo valor que cambio de proximidad
        }
        if (event.sensor.getType() == Sensor.TYPE_LIGHT)
        {
            //luxValue = event.values[0];

            textViewLux.setText(Float.toString(luxValue)); // seteo el ultimo valor que cambio el nivel de luz
=======
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximity = event.values[0];
            Log.i("valor proximidad:", String.valueOf(proximity) + event.timestamp);


            //la primera vez que se ejecute (y no este el sharedPreferences, el array va a estar vacio. Vamos a guardar los primeros 3 valores.
            // Dsp los vamos actualizando a medida que cambien. En 0 esta el valor mas actual, en 2 esta el mas viejo.
            // Cada vez que hago un add en la pos 0, desplaza los demas, y dsp elimino la pos 3 para mantener q sea de 3 posiciones.
            if(proximityValues.size() < MAX_LENGTH) {
                proximityValues.add(proximity);
            }

            if(proximity != proximityValues.get(0)){
                proximityValues.add(0, proximity);
                proximityValues.remove(MAX_LENGTH);
                update(PROXIMITY, proximityValues);
            }
            //acá llamarías al método para mostrar en pantalla, en el vector tenés los valores

            textViewProximity.setText(Float.toString(proximity));  //seteo el ultimo valor que cambio de proximidad

        }

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lux = event.values[0];
            Log.i("valor luz:", String.valueOf(lux));


            //la primera vez que se ejecute (y no este el sharedPreferences, el array va a estar vacio. Vamos a guardar los primeros 3 valores.
            // Dsp los vamos actualizando a medida que cambien. El 3 es un valor aleatorio que elegi. Si querés mostrar 10 valores en pantalla, entonces acá poner un 10.
            if(lightValues.size() < MAX_LENGTH) {
                lightValues.add(lux);
            }

            if(lux != lightValues.get(0) && Math.abs(event.values[0] - lightValues.get(0)) > 50) {
                lightValues.add(0, lux);
                lightValues.remove(MAX_LENGTH);
                update(LIGHT, lightValues);
            }

            //acá llamarías al método para mostrar en pantalla, en el vector tenés los valores
            textViewLux.setText(Float.toString(lux)); // seteo el ultimo valor que cambio el nivel de luz
        }

    }

    //Cada vez que varia el valor de los sensores, guardo en el SharedPreferences
    private void update(String type, List<Float> values) {

        for(int i = 0; i < values.size(); i++) {
            editor.putFloat(type+i, values.get(i));
        }
        editor.apply();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    //obtener el nombre del user
    //botón logout
    //botón registrar -> creás un hilo aparte donde levantás los datos de la pantalla y hacés 2 post con diferente type y datos.
    //actualizar las grillas con los datos de los sensores

}
