package com.example.barros_costa_tp2_2020;

public class User {
    private String name;
    private String email;
    private String lastName;
    private String password;
    private String dni;
    private int comision;
    private int group;
    private String token;

    public User(String name, String email, String lastName, String password, String dni, int comision, int group) {
        this.name = name;
        this.email = email;
        this.lastName = lastName;
        this.password = password;
        this.dni = dni;
        this.comision = comision;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
