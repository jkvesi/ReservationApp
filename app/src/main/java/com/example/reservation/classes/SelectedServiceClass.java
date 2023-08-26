package com.example.reservation.classes;

public class SelectedServiceClass {
    private String service, company, slot, provider;

    public String getSlot() {
        return slot;
    }

    public void setSlot(final String slot) {
        this.slot = slot;
    }

    public SelectedServiceClass() {
    }

    public SelectedServiceClass(final String service, final String company, String provider) {
        this.service = service;
        this.company = company;
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(final String provider) {
        this.provider = provider;
    }

    public String getService() {
        return service;
    }

    public void setService(final String service) {
        this.service = service;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(final String company) {
        this.company = company;
    }

}
