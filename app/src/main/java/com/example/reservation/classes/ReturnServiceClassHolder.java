package com.example.reservation.classes;

public class ReturnServiceClassHolder {
    public static ReturnServiceClassHolder instance;
    public ReturnServicesClass returnServices;

    public ReturnServiceClassHolder() {
        returnServices = new ReturnServicesClass();
    }

    public static ReturnServiceClassHolder getInstance() {
        if (instance == null) {
            instance = new ReturnServiceClassHolder();
        }
        return instance;
    }

    public ReturnServicesClass getReturnServices() {
        return returnServices;
    }

    public void setReturnServices(final ReturnServicesClass returnServices) {
        this.returnServices.setCompanyList(returnServices.getCompanyList());
        this.returnServices.setServiceList(returnServices.getServiceList());
        this.returnServices.setServiceTypeList(returnServices.getServiceTypeList());
    }
}
