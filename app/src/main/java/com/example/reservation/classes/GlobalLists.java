package com.example.reservation.classes;

import java.util.ArrayList;
import java.util.List;

public class GlobalLists {
    private static GlobalLists instance;

    private List<List<String>> masterList;
    private List<List<String>> reservationsList;
    private List<String> companiesResultList;

    public List<List<String>> getReservationsList() {
        return reservationsList;
    }

    public void setReservationsList(final List<List<String>> reservationsList) {
        this.reservationsList = reservationsList;
    }

    private GlobalLists() {
        masterList = new ArrayList<>();
    }

    public static GlobalLists getInstance() {
        if (instance == null) {
            instance = new GlobalLists();
        }
        return instance;
    }

    public List<String> getCompaniesResultList(){return  companiesResultList;}

    public List<List<String>> getMasterList() {
        return masterList;
    }

    public void addList(List<String> newList) {
        masterList.add(newList);
    }

    public void addReservationsList(List<String> newList){reservationsList.add(newList);}
    public void setCompaniesList (List<String> newList) { this.companiesResultList=newList;}
}

