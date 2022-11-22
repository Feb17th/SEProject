package com.example.backofficer.model;

import java.io.Serializable;

public class Vehicle implements Serializable {
    String type;
    String user;

    public Vehicle(String type, String user) {
        this.type = type;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
