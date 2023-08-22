package com.example.reservation.classes;

public class Reservation {
    private String user;
    private String date;
    private String slot;
    private String company;
    private String service;

    // Constructor
    public Reservation(String user, String date, String slot, String company, String service) {
        this.user = user;
        this.date = date;
        this.slot = slot;
        this.company = company;
        this.service = service;
    }

    // Getters
    public String getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }

    public String getSlot() {
        return slot;
    }

    public String getCompany() {
        return company;
    }

    public String getService() {
        return service;
    }
}

