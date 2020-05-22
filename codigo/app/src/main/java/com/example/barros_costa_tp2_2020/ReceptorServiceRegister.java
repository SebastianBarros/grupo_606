package com.example.barros_costa_tp2_2020;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceptorServiceRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String stringJsonData = null;
        JSONObject jsonData;

        try {
            stringJsonData = intent.getStringExtra("jsondata");
            JSONObject jsondata = new JSONObject(stringJsonData);
            Log.i("Logeo Server", "Datos del json"+ stringJsonData);
            Toast.makeText(context.getApplicationContext(),"Respuesta del server", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
