package com.example.barros_costa_tp2_2020;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String lastName;
    private String password;
    private String dni;
    private String course;
    private String group;
    private String token;

    public User(String name, String email, String lastName, String password, String dni, String course, String group) {
        this.name = name;
        this.email = email;
        this.lastName = lastName;
        this.password = password;
        this.dni = dni;
        this.course = course;
        this.group = group;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
