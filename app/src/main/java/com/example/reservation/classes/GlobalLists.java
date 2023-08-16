package com.example.reservation.classes;

import java.util.ArrayList;
import java.util.List;

public class GlobalLists {
    private static GlobalLists instance;

    private List<List<String>> masterList;

    private GlobalLists() {
        masterList = new ArrayList<>();
    }

    public static GlobalLists getInstance() {
        if (instance == null) {
            instance = new GlobalLists();
        }
        return instance;
    }

    public List<List<String>> getMasterList() {
        return masterList;
    }

    public void addList(List<String> newList) {
        masterList.add(newList);
    }
}

