package com.mahfuznow.assignment.model;

public class Person {
    private int id;
    private String latitude;
    private String longitude;
    private String altitude;
    private  String address;

    public Person() {
    }

    public Person(String latitude, String longitude, String altitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.address = address;
    }

    public Person(int id, String latitude, String longitude, String altitude, String address) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
