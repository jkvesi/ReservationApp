package com.example.reservation.implementations;

import com.example.reservation.classes.ReturnServiceClassHolder;
import com.example.reservation.classes.ReturnServicesClass;
import com.example.reservation.interfaces.OnDataFetchedListener;

// MyDataProcessor.java
public class OnDataFetchedClass implements OnDataFetchedListener {
    public OnDataFetchedClass() {
    }

    @Override
    public void onDataFetched( ReturnServicesClass data) {
        ReturnServiceClassHolder returnHolder = new ReturnServiceClassHolder();
        returnHolder.getInstance().setReturnServices(data);
    }
}

