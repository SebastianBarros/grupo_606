package com.example.barros_costa_tp2_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;


public class RegisterActivity extends AppCompatActivity {
    private static final String REGISTER_ACTION = "com.example.barros_costa_tp2_2020.intentservice.action.RESPONSE_REGISTER";
    private String URI_REGISTER = "http://so-unlam.net.ar/api/api/register";
    EditText editTextName;
    EditText editTextLastName;
    EditText editTextDni;
    EditText editTextPassword;
    EditText editTextEmail;
    EditText editTextCommision;
    EditText editTextGroup;
    ReceptorServiceRegister receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerBtn = findViewById(R.id.buttonRegister);
        Button goBackBtn = findViewById(R.id.buttonBackLogin);
        editTextName = findViewById(R.id.editTextRegisterName);
        editTextLastName = findViewById(R.id.editTextRegisterLastname);
        editTextEmail = findViewById(R.id.editTextRegisterEmail);
        editTextPassword = findViewById(R.id.editTextRegisterPassword);
        editTextDni = findViewById(R.id.editTextRegisterDni);
        editTextCommision = findViewById(R.id.editTextRegisterComission);
        editTextGroup = findViewById(R.id.editTextRegisterGroup);


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
        JSONObject jsonObject = new JSONObject();


        //if everything is ok
        //Check if there's an active network connection
        if(!InternetConnectionService.thereIsInternetConnection(RegisterActivity.this)){
            Toast.makeText(RegisterActivity.this, "No se encuentra conectado a Internet", Toast.LENGTH_LONG).show();
            return;
        }
        try {

            jsonObject.put("env","TEST");
            jsonObject.put("name",user.getName());
            jsonObject.put("lastname",user.getLastName());
            jsonObject.put("dni",user.getDni());
            jsonObject.put("email",user.getEmail());
            jsonObject.put("password",user.getPassword());
            jsonObject.put("commission",user.getCourse());
            jsonObject.put("group",user.getGroup());
            Intent intentRegister = new Intent(RegisterActivity.this, ServicesHttpPost.class);
            intentRegister.putExtra("action",REGISTER_ACTION);
            intentRegister.putExtra("uri",URI_REGISTER);
            intentRegister.putExtra("jsondata",jsonObject.toString());
            startService(intentRegister);
            configureBroadcastReceiver();

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        /*if(true) {
            RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, MenuActivity.class));
        }*/
    }

    private User createuser() {

        return new User(editTextName.getText().toString(), editTextLastName.getText().toString(), editTextDni.getText().toString(),
                        editTextEmail.getText().toString(), editTextPassword.getText().toString(), editTextCommision.getText().toString(),
                        editTextGroup.getText().toString());
    }

    private void configureBroadcastReceiver (){
        receiver = new ReceptorServiceRegister();
        IntentFilter filterRegister =  new IntentFilter(REGISTER_ACTION);
        filterRegister.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filterRegister);

    }





}
