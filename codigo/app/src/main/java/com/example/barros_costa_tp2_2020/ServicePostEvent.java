package com.example.barros_costa_tp2_2020;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServicePostEvent extends IntentService {

    private Exception mException;


    public ServicePostEvent() {
        super("ServicesHttpGET");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SERVICE","Service OnCreate()");
    }

    //este metodo inicia cuando se llama startService
    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        String action = extras.getString("action");
        String token = extras.getString("token");

        try {

            String uri = intent.getExtras().getString("uri");
            JSONObject jsonData = new JSONObject(intent.getExtras().getString("jsondata"));
            excecutePost(uri,jsonData,action,token);

        }
        catch (Exception e)
        {
            mException = e;
            return;
        }

    }

    protected void excecutePost(String uri, JSONObject jsonData,  String action,String token) throws JSONException {

        //llamado al metodo post, contiene el armado del paquete del POST

        String result = POST(uri,jsonData,token);

        if (result == null)
        {
            Log.e("Register Event", "Error en get: \n" + mException.toString());
            return;
        }
        if (result == "NO_OK"){
            Log.e("Register Event", "Response NO_OK");
            return;
        }

        //Intent que manda los datos del response al menuActivity
        Intent intentPostToBroadcast = new Intent(action);
        // Intent intentPostToBroadcast = new Intent(action);
        intentPostToBroadcast.putExtra("jsondata",result);
        sendBroadcast(intentPostToBroadcast);

    }

    private String POST(String uri, JSONObject jsonData,String token) throws JSONException {

        String result = null;
        URL url;
        HttpURLConnection urlConnection = null;
        int responseCode;
        Exception exceptionPOST = null;

        try {

            //se pasa la uri que viene por parametro hacia el metodo

            url = new URL(uri);

            //configuracion de la conexion

            urlConnection = (HttpURLConnection) url.openConnection();

            //configuro el header del json
            urlConnection.setRequestProperty("Content-type", "application/json; charset = UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("token", token);
            // tipo de request

            // aca creo el flujo de datos para el body del json

            DataOutputStream dataOutputStreamPost = new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStreamPost.write((jsonData.toString().getBytes("UTF-8")));
            dataOutputStreamPost.flush();
            dataOutputStreamPost.close();

            //aca envio el paquetito entero
            urlConnection.connect();

            //guardo el codigo http para ver que paso
            //se queda bloqueado el hilo hasta obtener respuesta

            responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                result = covertInputStreamToString(new InputStreamReader((urlConnection.getInputStream())));
            }
            else {
                result = "NO_OK";
            }

            urlConnection.disconnect();
            return result;

        } catch (Exception e) {
            return result;
        }
    }

    private String covertInputStreamToString(InputStreamReader inputStreamReader) throws IOException {
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer = new StringBuffer();
        String result;
        while((result = reader.readLine())!= null){
            stringBuffer.append(result);
        }
        return stringBuffer.toString();
    }

}

