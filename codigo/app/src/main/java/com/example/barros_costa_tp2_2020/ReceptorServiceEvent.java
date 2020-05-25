package com.example.barros_costa_tp2_2020;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;

public class ReceptorServiceEvent extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String stringJsonData = null;
        Gson gson = new Gson();
        ServerResponse responseLogin;
        stringJsonData = intent.getStringExtra("jsondata");
        responseLogin = gson.fromJson(stringJsonData, ServerResponse.class);
        if (responseLogin.getState().equals("success") ) {
            Toast.makeText(context.getApplicationContext(),"Evento registrado de forma exitosa", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(context.getApplicationContext(),"Problema para registrar el evento", Toast.LENGTH_LONG).show();
        }

    }
}
