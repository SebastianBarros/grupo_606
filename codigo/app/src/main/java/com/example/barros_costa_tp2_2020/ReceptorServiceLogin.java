package com.example.barros_costa_tp2_2020;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;

public class ReceptorServiceLogin extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String stringJsonData;
        Gson gson = new Gson();
        ServerResponse responseLogin;
        stringJsonData = intent.getStringExtra("jsondata");
        if (stringJsonData.equals("NO_OK")){
            Toast.makeText(context.getApplicationContext(),"Datos incorrectos. Vuelva a intentar", Toast.LENGTH_LONG).show();
            return;
        }
        responseLogin = gson.fromJson(stringJsonData, ServerResponse.class);
        if (responseLogin.getState().equals("success") ) {
            Toast.makeText(context.getApplicationContext(),"Login exitoso", Toast.LENGTH_LONG).show();
            Intent intentToMenu = new Intent(context,MenuActivity.class);
            intentToMenu.putExtra("token",responseLogin.getToken());
            context.startActivity(intentToMenu);
        }
    }


}
