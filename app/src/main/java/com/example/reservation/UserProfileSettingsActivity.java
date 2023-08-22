package com.example.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.reservation.classes.UserDataHolder;

public class UserProfileSettingsActivity extends AppCompatActivity {

    TextView firstName, lastName, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);

        firstName = findViewById(R.id.firstNameTextViewHolder);
        lastName = findViewById(R.id.lastNameTextViewHolder);
        email = findViewById(R.id.emailTextViewHolder);

        firstName.setText(UserDataHolder.getInstance().getUserData().getFirstName());
        lastName.setText(UserDataHolder.getInstance().getUserData().getLastName());
        email.setText(UserDataHolder.getInstance().getUserData().getEmail());
    }
}