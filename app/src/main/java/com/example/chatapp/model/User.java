package com.example.chatapp.model;

public class User {

    private String id;
    private String email;
    private String password;
    private String name;
    private String serName;
    private int age;
    private boolean online;

    public User(String id, String email, String password, String name, String serName, int age, boolean isOnline) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.serName = serName;
        this.age = age;
        this.online = isOnline;
    }

    public User(){

    }



    public String getSerName() {
        return serName;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isOnline() {
        return online;
    }
}
