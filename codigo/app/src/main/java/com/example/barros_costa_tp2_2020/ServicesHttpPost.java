package com.example.barros_costa_tp2_2020;

import android.app.IntentService;

import android.content.Intent;
import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ServicesHttpPost extends IntentService {

    private Exception mException = null;

    public ServicesHttpPost() {
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

        try {

            String uri = intent.getExtras().getString("uri");
            JSONObject jsonData = new JSONObject(intent.getExtras().getString("jsondata"));
            excecutePost(uri,jsonData);

        }
        catch (Exception e)
        {
            mException = e;
        }

    }

    protected void excecutePost(String uri, JSONObject jsonData) {

        Intent intentPostToRegisterActivity;

        //llamado al metodo post, contiene el armado del paquete del POST

        String result = POST(uri,jsonData);

        if (result == null)
        {
            Log.e("Register server", "Error en get: \n"+mException.toString());
            return;
        }
        if (result == "NO_OK"){
            Log.e("Register server", "Response NO_OK");
            return;
        }

        //Intent que manda los datos del response al registerActivity

        intentPostToRegisterActivity = new Intent("com.example.intentservice.intent.action.RESPONSE_REGISTER");
        intentPostToRegisterActivity.putExtra("jsondata",result);
        sendBroadcast(intentPostToRegisterActivity);
    }

    private String POST(String uri, JSONObject jsonData) {

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
            // tipo de request


            // aca creo el flujo de datos para el body del json



            Log.i("Logeo server", "se envio al servidor el body :" + jsonData.toString());

            DataOutputStream dataOutputStreamPost = new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStreamPost.write((jsonData.toString().getBytes("UTF-8")));
            dataOutputStreamPost.flush();
            dataOutputStreamPost.close();

            //aca envio el paquetito entero
            urlConnection.connect();

            //guardo el codigo http para ver que paso
            //se queda bloqueado el hilo hasta obtener respuesta

            responseCode = urlConnection.getResponseCode();
            Log.i("CODE", String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                result = covertInputStreamToString(new InputStreamReader((urlConnection.getInputStream())).toString());
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

    private String covertInputStreamToString(String toString) {
        return toString;
    }
}

