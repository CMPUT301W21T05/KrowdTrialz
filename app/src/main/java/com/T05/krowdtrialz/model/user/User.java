package com.T05.krowdtrialz.model.user;

import com.T05.krowdtrialz.model.experiment.Experiment;

public class User {
    private String name = "None";
    private String userName = "None";
    private String email = "None";
    private String id = "None";

    public User() {
    }

    public User(String id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof User)) {
            return false;
        }

        User c = (User) o;

        return c.getId().equals(this.getId());
    }
}