package com.example.reservation.classes;


import java.util.ArrayList;
import java.util.List;

public class GlobalScheduledAppintmentList {

    private static GlobalScheduledAppintmentList instance;
    private List<ScheduledAppointmentClass> appointmentsList;

    private GlobalScheduledAppintmentList() {
        appointmentsList = new ArrayList<>();
    }

    public static synchronized GlobalScheduledAppintmentList getInstance() {
        if (instance == null) {
            instance = new GlobalScheduledAppintmentList();
        }
        return instance;
    }

    public List<ScheduledAppointmentClass> getAppointments() {
        return appointmentsList;
    }

    public void addAppointment(ScheduledAppointmentClass appointments) {
        appointmentsList.add(appointments);
    }
}


