package com.example.backofficer.model;

import java.io.Serializable;

public class Information implements Serializable {
    private String emailOfProject;
    private String firstName;
    private String lastName;
    private String jobType;
    private String email;
    private String phoneNumber;
    private String identityCardNumber;
    private String fullName;
    private String userUID;

    public Information() {
        this.emailOfProject = "";
        this.firstName = "";
        this.lastName = "";
        this.jobType = "";
        this.email = "";
        this.phoneNumber = "";
        this.identityCardNumber = "";
        this.fullName = "";
        this.userUID = "";
    }

    public Information(String firstName, String lastName, String jobType, String email, String phoneNumber, String identityCardNumber, String fullName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobType = jobType;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.identityCardNumber = identityCardNumber;
        this.fullName = fullName;
    }

    public Information(String emailOfProject, String firstName, String lastName, String jobType, String email, String phoneNumber, String identityCardNumber, String fullName) {
        this.emailOfProject = emailOfProject;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobType = jobType;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.identityCardNumber = identityCardNumber;
        this.fullName = fullName;
    }

    public Information(String emailOfProject, String firstName, String lastName, String jobType, String email, String phoneNumber, String identityCardNumber, String fullName, String userUID) {
        this.emailOfProject = emailOfProject;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobType = jobType;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.identityCardNumber = identityCardNumber;
        this.fullName = fullName;
        this.userUID = userUID;
    }

    public String getEmailOfProject() {
        return emailOfProject;
    }

    public void setEmailOfProject(String emailOfProject) {
        this.emailOfProject = emailOfProject;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
