package com.example.barros_costa_tp2_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String LOGIN_ACTION = "com.example.barros_costa_tp2_2020.intent.action.RESPONSE_LOGIN";
    private String URI_LOGIN = "http://so-unlam.net.ar/api/api/login";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ReceptorServiceLogin receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //iniciar los botones y los text field. Crear el user si todo está OK y validar la contraseña
        //la petición rest manda sólo mail y pw, lo demás va en blanco.

        Button loginBtn = (Button) findViewById(R.id.buttonLogin);
        Button registerBtn = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registerRedirect();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });




    }

    private void registerRedirect() {
        final Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
        MainActivity.this.startActivity(registerIntent);
    }

    private void login() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        User user = new User(email, password);
        JSONObject jsonObject = new JSONObject();



        //TODO perfom REST request. If there's an error, we should inform the user. Maybe add an invisible label that becomes visible if an error occurs?

        //Check if there's an active network connection
        if(!InternetConnectionService.thereIsInternetConnection(MainActivity.this)){
            Toast.makeText(MainActivity.this, "No se encuentra conectado a Internet", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            jsonObject.put("env","TEST");
            jsonObject.put("email",user.getEmail());
            jsonObject.put("password",user.getPassword());

            Intent intentLogin = new Intent(MainActivity.this, ServicesHttpPost.class);
            intentLogin.putExtra("uri",URI_LOGIN);
            intentLogin.putExtra("jsondata",jsonObject.toString());
            intentLogin.putExtra("action",LOGIN_ACTION);
            configureBroadCastReceiver();
            startService(intentLogin);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*if (true) {
            final Intent menuIntent = new Intent(MainActivity.this, MenuActivity.class);
            menuIntent.putExtra("userEmail", email);
            MainActivity.this.startActivity(menuIntent);
        }
         */


    }
    private void configureBroadCastReceiver() {
        receiver = new ReceptorServiceLogin();
        IntentFilter filterRegister =  new IntentFilter(LOGIN_ACTION);
        filterRegister.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filterRegister);
    }

}



