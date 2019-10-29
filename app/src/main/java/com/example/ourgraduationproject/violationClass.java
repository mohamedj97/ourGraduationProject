package com.example.ourgraduationproject;

import android.location.Location;

import java.util.Date;

public class violationClass {
    private String location;
    private String Text;
    private Date date;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
