package com.example.backofficer.model;

import java.io.Serializable;

public class Vehicle implements Serializable {
    int registerNumber;
    String user;

    public Vehicle(){
        this.registerNumber = 0;
        this.user = "";
    }

    public Vehicle(int registerNumber, String user) {
        this.registerNumber = registerNumber;
        this.user = user;
    }

    public int getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(int registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
