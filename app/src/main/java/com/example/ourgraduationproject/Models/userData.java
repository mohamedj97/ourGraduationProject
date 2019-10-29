package com.example.ourgraduationproject.Models;

public class userData {
    public String username;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;
    public String securityQuestion;
    public String licenseNumber;
    public String address;
    public String gender;
    public String date;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public userData(String name , String username, String address, String gender, String date, String licenseNumber, String securityQuestion) {
        this.username = username;
        this.name=name;
        this.licenseNumber=licenseNumber;
        this.securityQuestion=securityQuestion;
        this.address = address;
        this.gender = gender;
        this.date=date;
    }
}
