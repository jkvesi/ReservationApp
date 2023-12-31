package com.example.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.functions.RetrieveDataFromDatabaseClass;
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

        View viewToAnimate = findViewById(R.id.welcomeLinearLayout);
        AnimatorSet colorAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.anim.welcome_animation);
        colorAnimation.setTarget(viewToAnimate);
        colorAnimation.start();

        UserDataClass userData = UserDataHolder.getInstance().getUserData();
        firstName = userData.getFirstName();
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
                    if(UserDataHolder.getInstance().getUserData().getFirstName() != null) {
                        name.setText(UserDataHolder.getInstance().getUserData().getFirstName());
                        TextView greeting = findViewById(R.id.greeting);
                        greeting.setVisibility(View.VISIBLE);
                    }
                    startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                    finish();
                }
                if (isLogout) {
                    if(UserDataHolder.getInstance().getUserData().getFirstName() != null) {
                        name.setText(userNameFromIntent);
                        TextView greeting = findViewById(R.id.greeting);
                        greeting.setVisibility(View.VISIBLE);
                    }
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, 3500);

    }

}