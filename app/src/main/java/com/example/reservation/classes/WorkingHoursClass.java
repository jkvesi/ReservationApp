package com.example.reservation.classes;

import java.util.Date;

public class WorkingHoursClass {
    private String date;
    private String openHour;
    private String closeHour;

    public WorkingHoursClass() {
    }

    public WorkingHoursClass(final String date, final String openHour, final String closeHour) {
        this.date = date;
        this.openHour = openHour;
        this.closeHour = closeHour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(final String openHour) {
        this.openHour = openHour;
    }

    public String getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(final String closeHour) {
        this.closeHour = closeHour;
    }
}
