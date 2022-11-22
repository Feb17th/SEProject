package com.example.backofficer.model;

import java.io.Serializable;
import java.sql.Time;

public class Task implements Serializable {
    private String fullName;
    private String description;
    private String time;
    private String vehicle;
    private String mcp;
    private String emailOfProject;

    public Task(){
        this.fullName = "";
        this.description = "";
        this.time = "";
        this.vehicle = "";
        this.mcp = "";
        this.emailOfProject = "";
    }

    public Task(String fullName, String description, String time, String vehicle, String mcp, String emailOfProject) {
        this.fullName = fullName;
        this.description = description;
        this.time = time;
        this.vehicle = vehicle;
        this.mcp = mcp;
        this.emailOfProject = emailOfProject;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getMcp() {
        return mcp;
    }

    public void setMcp(String mcp) {
        this.mcp = mcp;
    }

    public String getEmailOfProject() {
        return emailOfProject;
    }

    public void setEmailOfProject(String emailOfProject) {
        this.emailOfProject = emailOfProject;
    }
}
