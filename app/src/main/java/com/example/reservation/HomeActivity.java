package com.example.reservation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.reservation.classes.ReturnServiceClassHolder;
import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.functions.RetrieveDataFromDatabaseClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    Button newReservationBtn, previousReservationsBtn, profileSettingsBtn, logoutBtn;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String userName;
    Boolean switcher;
    DatabaseReference companyReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        RetrieveDataFromDatabaseClass retrieveData = new RetrieveDataFromDatabaseClass();
        retrieveData.getServices(companyReference, UserDataHolder.getInstance().getUserData());

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        if (user != null) {
            String userEmail = user.getEmail();
            reference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot snapshot) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        userName = userSnapshot.child("firstName").getValue(String.class);
                        switcher = userSnapshot.child("switcher").getValue(Boolean.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull final DatabaseError error) {

                }
            });

            auth = FirebaseAuth.getInstance();
            newReservationBtn = findViewById(R.id.newReservationBtn);
            previousReservationsBtn = findViewById(R.id.prevReservationBtn);
            profileSettingsBtn = findViewById(R.id.profileSettingsBtn);
            logoutBtn = findViewById(R.id.logOutBtn);

            newReservationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Intent intent = new Intent(HomeActivity.this, NewReservationActivity.class);
                    startActivity(intent);
                }
            });

            previousReservationsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Intent intent = new Intent(HomeActivity.this, PreviousReservationsActivity.class);
                    startActivity(intent);
                }
            });
            profileSettingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if(switcher) {
                        Intent intent = new Intent(HomeActivity.this, ProviderProfileSettingsActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(HomeActivity.this, UserProfileSettingsActivity.class);
                        startActivity(intent);
                    }
                }
            });




            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    auth.signOut();
                    Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
                    intent.putExtra("greeting", "Goodby");
                    intent.putExtra("logout", true);
                    intent.putExtra("userName", userName);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}