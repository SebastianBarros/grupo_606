package com.example.barros_costa_tp2_2020;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceptorServiceRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String stringJsonData;
        Gson gson = new Gson();
        ServerResponse responseRegister;
        stringJsonData = intent.getStringExtra("jsondata");
        if (stringJsonData.equals("NO_OK")) {
            Toast.makeText(context.getApplicationContext(), "Datos incorrectos. Vuelva a intentar", Toast.LENGTH_LONG).show();
            return;
        }
        responseRegister = gson.fromJson(stringJsonData, ServerResponse.class);
        if (responseRegister.getState().equals("success")) {
            Toast.makeText(context.getApplicationContext(), "Registro exitoso", Toast.LENGTH_LONG).show();
            Intent intentToMenu = new Intent(context, MenuActivity.class);
            intentToMenu.putExtra("response", responseRegister);
            context.startActivity(intentToMenu);
        }
    }
}

