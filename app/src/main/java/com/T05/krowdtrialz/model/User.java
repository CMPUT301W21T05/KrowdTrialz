package com.T05.krowdtrialz.model;

public class User {
    private String name;
    private String id;
    private String email;

    public User(String name, String id, String email) {
        this.name = name;
        this.id = id;
        this.email = email;
    }

    public String getName(){
        return name;
    }
}
