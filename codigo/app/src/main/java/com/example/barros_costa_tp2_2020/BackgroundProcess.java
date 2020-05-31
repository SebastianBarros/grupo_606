package com.example.barros_costa_tp2_2020;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundProcess extends Service {


    private static final String URI_EVENT = "http://so-unlam.net.ar/api/api/event";
    private static final String EVENT_ACTION = "com.example.barros_costa_tp2_2020.intent.action.RESPONSE_EVENT";
    private static final Integer intervalo = 60000; //1min
    private String token;
    private JSONObject jsonObject;
    private ReceptorServiceEvent receiver = new ReceptorServiceEvent();
    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        token = bundle.getString("token");

        execute();
        return 1;

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        timer.cancel();
    }



    private void execute() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("env", "DEV");
                    jsonObject.put("type_events", "Background process");
                    jsonObject.put("state", "ACTIVO");
                    jsonObject.put("description", "Proceso corriendo en el background");
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }

                Intent intentEvent = new Intent(BackgroundProcess.this, ServicePostEvent.class);
                intentEvent.putExtra("uri", URI_EVENT);
                intentEvent.putExtra("token", token);
                intentEvent.putExtra("jsondata", jsonObject.toString());
                intentEvent.putExtra("action", EVENT_ACTION);

                configureBroadCastReceiver();

                startService(intentEvent);


                Log.i("Thread", "Ocurrí");
            }
        };
        timer.scheduleAtFixedRate(task, 0, intervalo);
    }

    private void configureBroadCastReceiver() {
        IntentFilter filterRegister = new IntentFilter(EVENT_ACTION);
        filterRegister.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filterRegister);
    }

}
