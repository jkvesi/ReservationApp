package com.example.reservation.classes;

public class ServiceSubtypeClass {
    private String subtype;
    private int prize;
    private double duration;

    public ServiceSubtypeClass(final String subtype, final int prize, final double duration) {
        this.subtype = subtype;
        this.prize = prize;
        this.duration = duration;
    }

    public ServiceSubtypeClass() {
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(final String subtype) {
        this.subtype = subtype;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(final int prize) {
        this.prize = prize;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(final double duration) {
        this.duration = duration;
    }
}
