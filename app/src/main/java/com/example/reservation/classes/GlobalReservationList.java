package com.example.reservation.classes;

import java.util.ArrayList;
import java.util.List;

public class GlobalReservationList {

    private static GlobalReservationList instance;
    private List<Reservation> reservations;

    private GlobalReservationList() {
        reservations = new ArrayList<>();
    }

    public static synchronized GlobalReservationList getInstance() {
        if (instance == null) {
            instance = new GlobalReservationList();
        }
        return instance;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
}

