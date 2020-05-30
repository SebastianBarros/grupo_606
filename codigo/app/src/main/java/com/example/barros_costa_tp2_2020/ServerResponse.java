package com.example.barros_costa_tp2_2020;

import java.io.Serializable;

public class ServerResponse implements Serializable {

    private String token;
    private String state;
    private String env;



    public ServerResponse(String token, String state, String env,String msg) {
        this.token = token;
        this.state = state;
        this.env = env;
    }

    public String getToken() {
        return token;
    }

    public String getState() {
        return state;
    }

    //petición login
    //petición registro
    //petición evento

}
