package com.example.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onLogClick(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void onRegisterClick(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}