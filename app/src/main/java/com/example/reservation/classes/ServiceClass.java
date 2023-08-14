package com.example.reservation.classes;

public class ServiceClass {
    private String companyName;
    private String service;

    public ServiceClass(final String companyName, final String service) {
        this.companyName = companyName;
        this.service = service;
    }

    public ServiceClass() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }

    public String getService() {
        return service;
    }

    public void setService(final String service) {
        this.service = service;
    }
}
