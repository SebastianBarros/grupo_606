package com.example.barros_costa_tp2_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerBtn = (Button) findViewById(R.id.buttonRegister);
        Button goBackBtn = (Button) findViewById(R.id.buttonBackLogin);

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

            }
        });

    }

    private void register() {
        User user = createuser();
        //do stuff with the user
        //if everything is ok
        //Check if there's an active network connection
        if(!InternetConnectionService.thereIsInternetConnection(RegisterActivity.this)){
            Toast.makeText(RegisterActivity.this, "No se encuentra conectado a Internet", Toast.LENGTH_LONG).show();
            return;
        }
        if(true) {
            RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, MenuActivity.class));
        }
    }

    private User createuser() {
        TextView name = findViewById(R.id.editTextRegisterName);
        TextView lastName = findViewById(R.id.editTextRegisterLastname);
        TextView dni = findViewById(R.id.editTextRegisterDni);
        TextView mail =  findViewById(R.id.editTextRegisterEmail);
        TextView password =  findViewById(R.id.editTextRegisterPassword);
        TextView course = findViewById(R.id.editTextRegisterComission);
        TextView group = findViewById(R.id.editTextRegisterGroup);
        return new User(name.getText().toString(), lastName.getText().toString(), dni.getText().toString(),
                        mail.getText().toString(), password.getText().toString(), course.getText().toString(),
                        group.getText().toString());
    }


}
