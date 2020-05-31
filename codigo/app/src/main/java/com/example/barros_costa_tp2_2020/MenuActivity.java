package com.example.barros_costa_tp2_2020;

import android.annotation.SuppressLint;
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

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends AppCompatActivity implements SensorEventListener {

    private static final String PROXIMITY = "PROXIMITY";
    private static final String LIGHT = "LIGHT";
    private static final int MAX_LENGTH = 3;
    private static final String URI_EVENT = "http://so-unlam.net.ar/api/api/event";
    private static final String EVENT_ACTION = "com.example.barros_costa_tp2_2020.intent.action.RESPONSE_EVENT";
    private static final Integer DEFAULT = 404;

    // variables de managment de sensores
    private SensorManager sensorManagerLux;
    private SensorManager sensorManagerProximity;
    //objeto sensor donde seran registrados cada sensor
    private Sensor lux;
    private Sensor proximity;

    private Intent intentReceived;
    private Bundle extrasReceived;
    private Intent backgroundService;
    private String token;
    JSONObject jsonObject;
    private List<TextView> lightArray = new ArrayList<>();
    private List<TextView> proximityArray = new ArrayList<>();
    private TextView textViewLux;
    private TextView textViewLux1;
    private TextView textViewLux2;
    private TextView textViewProximity;
    private TextView textViewProximity1;
    private TextView textViewProximity2;
    private float proximityValue;
    private float luxValue;
    private ArrayList<Float> stackluxValue = new ArrayList<Float>();
    ReceptorServiceEvent receiver;// variables tipo float para almacenar los valores captados por el sensor

    private List<Float> lightValues = new ArrayList<>();
    private List<Float> proximityValues = new ArrayList<>();


    //esto puede que corresponda a otra clase
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        LinearLayout lightLayout = findViewById(R.id.linearLayoutLux);
        LinearLayout proximityLayout = findViewById(R.id.linearLayoutProximity);
        Button saveLightData = findViewById(R.id.buttonRegisterLuxSensor);
        Button saveProximityData = findViewById(R.id.buttonRegisterProximitySensor);
        Button goBackToLogin = findViewById(R.id.buttonBack);


        sensorManagerLux = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lux = sensorManagerLux.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManagerProximity = (SensorManager) getSystemService((Context.SENSOR_SERVICE));
        proximity = sensorManagerProximity.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        intentReceived = getIntent();
        extrasReceived = intentReceived.getExtras();
        token = extrasReceived.getString("token");

        textViewLux = findViewById(R.id.textViewLux);
        textViewLux1 = findViewById(R.id.textViewLux1);
        textViewLux2 = findViewById(R.id.textViewLux2);

        lightArray.add(0, textViewLux);
        lightArray.add(1, textViewLux1);
        lightArray.add(2, textViewLux2);

        textViewProximity = findViewById(R.id.textViewProximity);
        textViewProximity1 = findViewById(R.id.textViewProximity1);
        textViewProximity2 = findViewById(R.id.textViewProximity2);

        proximityArray.add(0, textViewProximity);
        proximityArray.add(1, textViewProximity1);
        proximityArray.add(2, textViewProximity2);

        sharedPref = MenuActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        loadSensorData();

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
    }

    private void startBackgroundService() {
        backgroundService = new Intent(MenuActivity.this, BackgroundProcess.class);
        backgroundService.putExtra("token", token);
        startService(backgroundService);
    }

    @SuppressLint("CommitPrefEdits")
    protected void onResume() {
        super.onResume();
        //los sensores no haran nada hasta que la actividad este en primer plano.
        // aca se registran, no olvidar de-registrarlos porque en si se la actividad va onPaused y no lo haces siguen corriendo.
        //tengo que ver como meter esto en dos hilos distintos. por ahora no llego a colgarse pero deberia ir en cada hilo tengo entendido
        sensorManagerLux.registerListener(this, lux, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManagerProximity.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);

        //Inicias el sharedPref y creas un editor
        sharedPref = MenuActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        startBackgroundService();
    }



    protected void onPause() {
        super.onPause();
        sensorManagerLux.unregisterListener(this);
        sensorManagerProximity.unregisterListener(this);
    }

    protected void onStop() {
        super.onStop();
        stopService(backgroundService);
    }

    private void goBackToLogin() {
        MenuActivity.this.startActivity(new Intent(MenuActivity.this, MainActivity.class));
    }


    private void loadSensorData() {

        for(int i = 0; i < lightArray.size(); i++) {
            lightArray.get(i).setText(String.valueOf(sharedPref.getFloat("LIGHT" + i, 0)));
        }

        for(int i = 0; i < proximityArray.size(); i++) {
            proximityArray.get(i).setText(String.valueOf(sharedPref.getFloat("PROXIMITY" + i, 0)));
        }
    }

    private void saveProximityData() throws JSONException {
        jsonObject = new JSONObject();
        jsonObject.put("env", "DEV");
        jsonObject.put("type_events", "Proximity Sensor");
        jsonObject.put("state", "ACTIVO");
        jsonObject.put("description", String.valueOf(proximityValue));

        Intent intentEvent = new Intent(MenuActivity.this, ServicePostEvent.class);
        intentEvent.putExtra("uri", URI_EVENT);
        intentEvent.putExtra("token", token);
        intentEvent.putExtra("jsondata", jsonObject.toString());
        intentEvent.putExtra("action", EVENT_ACTION);
        configureBroadCastReceiver();
        startService(intentEvent);
    }

    private void configureBroadCastReceiver() {
        receiver = new ReceptorServiceEvent();
        IntentFilter filterRegister = new IntentFilter(EVENT_ACTION);
        filterRegister.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filterRegister);
    }

    private void saveLightData() throws JSONException {
        jsonObject = new JSONObject();
        jsonObject.put("env", "DEV");
        jsonObject.put("type_events", "Lux Sensor");
        jsonObject.put("state", "ACTIVO");
        jsonObject.put("description", String.valueOf(luxValue));

        Intent intentEvent = new Intent(MenuActivity.this, ServicePostEvent.class);
        intentEvent.putExtra("uri", URI_EVENT);
        intentEvent.putExtra("token", token);
        intentEvent.putExtra("jsondata", jsonObject.toString());
        intentEvent.putExtra("action", EVENT_ACTION);
        configureBroadCastReceiver();
        startService(intentEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                proximityValue = event.values[0];
                Log.i("valor proximidad:", String.valueOf(proximity) + event.timestamp);


                //la primera vez que se ejecute (y no este el sharedPreferences, el array va a estar vacio. Vamos a guardar los primeros 3 valores.
                // Dsp los vamos actualizando a medida que cambien. En 0 esta el valor mas actual, en 2 esta el mas viejo.
                // Cada vez que hago un add en la pos 0, desplaza los demas, y dsp elimino la pos 3 para mantener q sea de 3 posiciones.
                if (proximityValues.size() < MAX_LENGTH) {
                    proximityValues.add(proximityValue);
                }

                if (proximityValue == proximityValues.get(0)) {
                    return;
                }
                    proximityValues.add(0, proximityValue);
                    proximityValues.remove(MAX_LENGTH);
                    update(PROXIMITY, proximityValues);

                //acá llamarías al método para mostrar en pantalla, en el vector tenés los valores

                for(int i = 0; i < proximityValues.size(); i++){
                    proximityArray.get(i).setText(proximityValues.get(i).toString());
                }
            }

            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                luxValue = event.values[0];
                Log.i("valor luz:", String.valueOf(lux));


                //la primera vez que se ejecute (y no este el sharedPreferences, el array va a estar vacio. Vamos a guardar los primeros 3 valores.
                // Dsp los vamos actualizando a medida que cambien. El 3 es un valor aleatorio que elegi. Si querés mostrar 10 valores en pantalla, entonces acá poner un 10.
                if (lightValues.size() < MAX_LENGTH) {
                    lightValues.add(luxValue);
                }

                if(Math.abs(event.values[0] - lightValues.get(0)) <= 50) {
                    return;
                }

                lightValues.add(0, luxValue);
                lightValues.remove(MAX_LENGTH);
                update(LIGHT, lightValues);

                for(int i = 0; i < lightValues.size(); i++){
                    lightArray.get(i).setText(lightValues.get(i).toString());
                }
            }


    }

    //Cada vez que varia el valor de los sensores, guardo en el SharedPreferences

    private void update(String type, List<Float> values) {

        for (int i = 0; i < values.size(); i++) {
            editor.putFloat(type + i, values.get(i));
        }
        editor.apply();
    }

}
