package com.T05.krowdtrialz.model.user;

public class User {
    private String name;
    private String userName;
    private String email;
    private String id;

    public User() {
    }

    public User(String id) {
        this(null, null, null, id);
    }

    public User(String name, String userName, String email, String id) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getId() { return id; }
}