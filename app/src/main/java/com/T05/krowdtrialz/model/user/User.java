package com.T05.krowdtrialz.model.user;

import java.util.Objects;

/**
 * User of the KrowdTrialz applicaiton. The user is not forced to enter
 * their information, so "None" is the default for all fields.
 */
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

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public int hashCode() {
        return Objects.hash(name, userName, email, id);
    }
}