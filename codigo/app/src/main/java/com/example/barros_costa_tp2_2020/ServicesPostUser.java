package com.example.barros_costa_tp2_2020;

import android.app.IntentService;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class ServicesPostUser extends IntentService {

    private Exception mException = null;

    public ServicesPostUser() {
        super("");
    }

    @Override
        public void onCreate() {
        super.onCreate();
    }

    //este metodo inicia cuando se llama startService
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String action = extras.getString("action");

        try {

            //recibo los parametros que manda el intent que envio el inicio de este servicio

            String uri = intent.getExtras().getString("uri");
            JSONObject jsonData = new JSONObject(intent.getExtras().getString("jsondata"));

            // para saber el tipo de accion que me pidio el inicio de servicio -> LOGIN, REGISTRO
            excecutePost(uri,jsonData,action);
        }
        catch (Exception e)
        {
            mException = e;
            return;
        }
    }

    protected void excecutePost(String uri, JSONObject jsonData, String action) {

        //llamado al metodo post, contiene el armado del paquete del POST

        String result = POST(uri,jsonData);

        if (result == null)
        {
            Log.e("SERVER","error de conexion con server");
            return;
        }

        //Intent que manda los datos del response al registerActivity
        Intent intentPostToBroadcast = new Intent(action);
        intentPostToBroadcast.putExtra("jsondata",result);
        sendBroadcast(intentPostToBroadcast);

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
            else
            {
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

