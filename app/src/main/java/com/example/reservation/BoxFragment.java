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
import com.example.reservation.classes.ReturnServiceClassHolder;
import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.functions.DeleteItemsInDatabaseClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BoxFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        DeleteItemsInDatabaseClass deleteItemsInDatabaseClass = new DeleteItemsInDatabaseClass();
        View rootView = inflater.inflate(R.layout.fragment_box, container, false);

        LinearLayout containerLayout = rootView.findViewById(R.id.containerLayout);

        int listSize = GlobalLists.getInstance().getMasterList().size();
        for (int i = 0; i < listSize; i++) {

            View settingsView = inflater.inflate(R.layout.activity_provider_profile_settings, containerLayout, false);

                View boxView = inflater.inflate(R.layout.item_box_layout, containerLayout, false);

                Button deleteBtn = boxView.findViewById(R.id.deleteServiceBtn);
                TextView companyNameTextView = boxView.findViewById(R.id.companyDisplay);
                TextView serviceTextView = boxView.findViewById(R.id.serviceDisplay);
                TextView serviceTypeTextView = boxView.findViewById(R.id.serviceTypeDisplay);

                companyNameTextView.setText(GlobalLists.getInstance().getMasterList().get(i).get(0));
                serviceTextView.setText(GlobalLists.getInstance().getMasterList().get(i).get(1));
                serviceTypeTextView.setText(GlobalLists.getInstance().getMasterList().get(i).get(2));

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        String serviceType = serviceTypeTextView.getText().toString();
                        String company = companyNameTextView.getText().toString();
                        String service = serviceTextView.getText().toString();

                        UserDataClass user = UserDataHolder.getInstance().getUserData();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getFirstName()).child("Service").
                                child(company).child(service).child(serviceType);
                       deleteItemsInDatabaseClass.DeleteServices(reference, service, serviceType,company, user);
                        containerLayout.removeView(boxView);

                    }
                });
                containerLayout.addView(boxView);
            }

        return rootView;
    }
}
