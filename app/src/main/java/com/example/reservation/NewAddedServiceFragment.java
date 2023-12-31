package com.example.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reservation.classes.GlobalLists;
import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

            Button deleteBtn = boxView.findViewById(R.id.deleteServiceBtn);
            TextView companyNameTextView = boxView.findViewById(R.id.companyDisplay);
            TextView serviceTextView = boxView.findViewById(R.id.serviceDisplay);
            TextView serviceTypeTextView = boxView.findViewById(R.id.serviceTypeDisplay);

            // Set data from the lists to the TextViews
            //String display = ReturnServiceClassHolder.getInstance().getReturnServices().getServiceList().get(i) + ReturnServiceClassHolder.getInstance().getReturnServices().getServiceTypeList().get(j);
            companyNameTextView.setText(this.company);
            serviceTextView.setText(this.service);
            serviceTypeTextView.setText(this.serviceType);


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String serviceType = serviceTypeTextView.getText().toString();
                String company = companyNameTextView.getText().toString();
                String service = serviceTextView.getText().toString();

                UserDataClass user = UserDataHolder.getInstance().getUserData();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getFirstName()).child("Service").
                        child(company).child(service).child(serviceType);
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(final Void unused) {
                                containerLayout.removeView(boxView);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Countries").child(user.getCountry()).child(user.getCity()).
                                        child(service).child(serviceType).child(company);

                                DatabaseReference referenceToDelete = databaseReference.getParent();
                                referenceToDelete.removeValue();

                                Toast.makeText(getActivity(), "Item deleted successfully!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull final Exception e) {
                                Toast.makeText(getActivity(), "Problems with deleting!", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

            // Add the inflated box layout to the container
            containerLayout.addView(boxView);

        return rootView;
    }
}
