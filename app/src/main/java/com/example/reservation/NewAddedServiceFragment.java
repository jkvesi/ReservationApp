package com.example.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reservation.classes.GlobalLists;

public class NewAddedServiceFragment extends Fragment {
    String company;
    String service;
    String serviceType;

    public NewAddedServiceFragment(final String company, final String service, final String serviceType) {
        this.company = company;
        this.service = service;
        this.serviceType = serviceType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_box, container, false);

        // Get a reference to the container layout where you want to add the boxes
        LinearLayout containerLayout = rootView.findViewById(R.id.containerLayout);

        // Loop to add the box layout multiple times

            // Inflate the box layout
            View boxView = inflater.inflate(R.layout.item_box_layout, containerLayout, false);

            TextView companyNameTextView = boxView.findViewById(R.id.companyDisplay);
            TextView serviceTextView = boxView.findViewById(R.id.serviceDisplay);
            TextView serviceTypeTextView = boxView.findViewById(R.id.serviceTypeDisplay);

            // Set data from the lists to the TextViews
            //String display = ReturnServiceClassHolder.getInstance().getReturnServices().getServiceList().get(i) + ReturnServiceClassHolder.getInstance().getReturnServices().getServiceTypeList().get(j);
            companyNameTextView.setText(this.company);
            serviceTextView.setText(this.service);
            serviceTypeTextView.setText(this.serviceType);

            // Add the inflated box layout to the container
            containerLayout.addView(boxView);

        return rootView;
    }
}
