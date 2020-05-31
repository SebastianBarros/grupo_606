package com.example.barros_costa_tp2_2020;

import java.io.Serializable;

public class ServerResponse implements Serializable {

    private String token;
    private String state;
    private String env;
    private Event event;



    public ServerResponse(String token, String state, String env,String msg, String t, String s, String d) {
        this.token = token;
        this.state = state;
        this.env = env;
        this.event = new Event(t, s, d);
    }

    public String getToken() {
        return token;
    }

    public String getState() {
        return state;
    }

    public String getEventType() { return event.getType_events(); }

    //petición login
    //petición registro
    //petición evento

}
