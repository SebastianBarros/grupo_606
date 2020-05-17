package com.example.barros_costa_tp2_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //iniciar los botones y los text field. Crear el user si todo está OK y validar la contraseña
        //la petición rest manda sólo mail y pw, lo demás va en blanco.

        Button loginBtn = (Button) findViewById(R.id.buttonLogin);
        Button registerBtn = (Button) findViewById(R.id.buttonRegister);
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
        String email = findViewById(R.id.editTextEmail).toString();
        String password = findViewById(R.id.editTextPassword).toString();

        //TODO perfom REST request. If there's an error, we should inform the user. Maybe add an invisible label that becomes visible if an error occurs?

        if(true) {
            final Intent menuIntent = new Intent( MainActivity.this, MenuActivity.class);
            menuIntent.putExtra("userEmail", email);
            MainActivity.this.startActivity(menuIntent);
        }

    }

}
