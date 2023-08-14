package com.example.reservation.classes;

import java.util.List;

public class ReturnServicesClass {
    private List<String> companyList;
    private List<String> serviceList;
    private List<String> serviceTypeList;

    public ReturnServicesClass() {
    }

    public ReturnServicesClass(final List<String> countryList, final List<String> serviceList, final List<String> serviceTypeList) {
        this.companyList = countryList;
        this.serviceList = serviceList;
        this.serviceTypeList = serviceTypeList;
    }

    public List<String> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(final List<String> countryList) {
        this.companyList = countryList;
    }

    public List<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(final List<String> serviceList) {
        this.serviceList = serviceList;
    }

    public List<String> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(final List<String> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }
}
