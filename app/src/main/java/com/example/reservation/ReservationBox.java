package com.example.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import com.example.reservation.classes.GlobalLists;
import com.example.reservation.classes.GlobalReservationList;
import com.example.reservation.classes.ReturnServiceClassHolder;
import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReservationBox extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.reservation_fragment_box, container, false);

        // Get a reference to the container layout where you want to add the boxes
        LinearLayout containerLayout = rootView.findViewById(R.id.reservationContainerLayout);

        int listSize = GlobalReservationList.getInstance().getReservations().size();
        for (int i = 0; i < listSize; i++) {

            View settingsView = inflater.inflate(R.layout.activity_previous_reservation, containerLayout, false);

            View boxView = inflater.inflate(R.layout.reservation_box_layout, containerLayout, false);

            TextView company= boxView.findViewById(R.id.companyHolder);
            TextView serviceType = boxView.findViewById(R.id.serviceTypeHolder);
            TextView slot = boxView.findViewById(R.id.slotHolder);
            TextView date = boxView.findViewById(R.id.dateHolder);

            company.setText(GlobalReservationList.getInstance().getReservations().get(i).getCompany());
            serviceType.setText(GlobalReservationList.getInstance().getReservations().get(i).getService());
            date.setText(GlobalReservationList.getInstance().getReservations().get(i).getDate());
            slot.setText(GlobalReservationList.getInstance().getReservations().get(i).getSlot());
            containerLayout.addView(boxView);
        }

        return rootView;
    }
}
