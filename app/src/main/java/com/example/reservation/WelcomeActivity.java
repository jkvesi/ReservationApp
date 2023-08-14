package com.example.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {

    String firstName, lastName, country, city, phone, email;
    Boolean switcher;

    TextView greeting;
    TextView name;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        UserDataClass userData = UserDataHolder.getInstance().getUserData();
        firstName = userData.getFirstName();
/*
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        if(user != null){
            String userEmail = user.getEmail();
            reference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot snapshot) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        firstName = userSnapshot.child("firstName").getValue(String.class);
                        lastName = userSnapshot.child("lastName").getValue(String.class);
                        country = userSnapshot.child("country").getValue(String.class);
                        city = userSnapshot.child("city").getValue(String.class);
                        phone = userSnapshot.child("phone").getValue(String.class);
                        switcher = userSnapshot.child("switcher").getValue(Boolean.class);
                        email = userSnapshot.child("email").getValue(String.class);

                        UserDataClass userData = new UserDataClass(firstName, lastName, switcher,phone, country,city, email);
                        UserDataHolder.getInstance().setUserData(userData);
                    }
                }

                @Override
                public void onCancelled(@NonNull final DatabaseError error) {

                }
            });
        }*/
        Intent intent= getIntent();
        String message = intent.getStringExtra("greeting");
        greeting = findViewById(R.id.greeting);
        greeting.setText(message);

        name = findViewById(R.id.userName);

        Boolean isLogin = intent.getBooleanExtra("login", false);
        Boolean isLogout = intent.getBooleanExtra("logout", false);

        String userNameFromIntent = intent.getStringExtra("userName");
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (isLogin) {
                        name.setText(UserDataHolder.getInstance().getUserData().getFirstName());
                        startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                        finish();
                    }
                    if (isLogout) {
                        name.setText(userNameFromIntent);
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }, 3000);

        }

    }

