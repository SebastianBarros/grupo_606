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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public User(String name, String lastName, String dni, String email, String password, String course, String group) {
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
