package com.example.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.reservation.functions.RetrieveDataFromDatabaseClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NewReservationActivity extends AppCompatActivity {

    Spinner countrySpinner;
    FirebaseDatabase database;
    DatabaseReference countryReference;
    List<String> countriesList = new ArrayList<>();
    String chosenCountry, chosenCity;
    List<String> citiesList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reservation);

        database = FirebaseDatabase.getInstance();
        countryReference = database.getReference("Countries");

        countrySpinner = findViewById(R.id.countrySpinner);
        RetrieveDataFromDatabaseClass countriesFromDB = new RetrieveDataFromDatabaseClass();
        RetrieveDataFromDatabaseClass citiesFromDB = new RetrieveDataFromDatabaseClass();
        countriesList = countriesFromDB.getListOfCountriesFromDB(countryReference);

        ImageView iconImageView = findViewById(R.id.homeIcon);
        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the new activity
                Intent intent = new Intent(NewReservationActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                chosenCountry = adapterView.getItemAtPosition(i).toString();
                countrySpinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) {
                Toast.makeText(NewReservationActivity.this, "Nista nije odabrano",
                        Toast.LENGTH_SHORT).show();
            }
         });

      //  citiesList = citiesFromDB.getListOfCities(countryReference, chosenCountry);
    }
}