package com.example.barros_costa_tp2_2020;

public class Event {

    private String type_events;
    private String state;
    private String description;

    public Event (String t, String s, String d) {
        type_events = t;
        state = s;
        description = d;
    }

    public String getType_events() {
        return type_events;
    }
}
