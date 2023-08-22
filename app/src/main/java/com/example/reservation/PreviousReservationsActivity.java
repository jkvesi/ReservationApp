package com.example.reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.reservation.classes.GlobalLists;

public class PreviousReservationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_reservation);
       // GlobalLists.getInstance().getReservationsList();

        ImageView iconImageView = findViewById(R.id.homeIcon);
        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the new activity
                GlobalLists.getInstance().getMasterList().clear();
                Intent intent = new Intent(PreviousReservationsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.previousReservationContainer, new ReservationBox() );
        fragmentTransaction.commit();
        /*if (savedInstanceState == null) {
           getSupportFragmentManager().beginTransaction()
                    .add(R.id.previousReservationContainer, new ReservationBox())
                    .commit();
        }*/
    }

}