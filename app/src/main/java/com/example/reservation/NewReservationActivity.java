package com.example.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reservation.classes.GlobalLists;
import com.example.reservation.functions.RetrieveDataFromDatabaseClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NewReservationActivity extends AppCompatActivity {

    Spinner countrySpinner, citySpinner;
    FirebaseDatabase database;
    DatabaseReference countryReference;
    List<String> countriesList = new ArrayList<>();
    List<String> citiesList = new ArrayList<>();
    List<String> servicesList = new ArrayList<>();
    List<String> serviceTypeList = new ArrayList<>();
    String selectedCountry, selectedCity, selectedService, selectedServiceType, selectedCompany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reservation);

        database = FirebaseDatabase.getInstance();
        countryReference = database.getReference("Countries");

       // countrySpinner = findViewById(R.id.countrySpinner);
       // citySpinner = findViewById(R.id.citySpinner);
        RetrieveDataFromDatabaseClass retrieveFromDatabase = new RetrieveDataFromDatabaseClass();
        RetrieveDataFromDatabaseClass citiesFromDB = new RetrieveDataFromDatabaseClass();
        countriesList = retrieveFromDatabase.getListOfCountriesFromDB(countryReference);

        ImageView iconImageView = findViewById(R.id.homeIcon);
        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the new activity
                GlobalLists.getInstance().getMasterList().clear();
                Intent intent = new Intent(NewReservationActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


        TextView selectCountry = findViewById(R.id.countrySelector);
        TextView selectCity = findViewById(R.id.citySelector);
        TextView selectService = findViewById(R.id.serviceSelector);
        TextView selectServiceType = findViewById(R.id.serviceTypeSelector);


        selectCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(NewReservationActivity.this, selectCountry);

                for (String country : countriesList) {
                    popupMenu.getMenu().add(country);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem menuItem) {
                        selectedCountry = menuItem.getTitle().toString();
                        selectCountry.setText(selectedCountry);
                       // String message = getString(R.string.sele)
                        citiesList = retrieveFromDatabase.getListOfCitiesByCountryFromDB(countryReference, selectedCountry);
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(NewReservationActivity.this, selectCity);

                for (String city : citiesList) {
                    popupMenu.getMenu().add(city);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem menuItem) {
                        selectedCity = menuItem.getTitle().toString();
                        selectCity.setText(selectedCity);
                        servicesList = retrieveFromDatabase.getListOfServicesByCitiesAndCountryFromDB(countryReference, selectedCountry, selectedCity);
                        // String message = getString(R.string.sele)
                        return false;
                    }
                });

                popupMenu.show();
            }
        });


        selectService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(NewReservationActivity.this, selectService);

                for (String service : servicesList) {
                    popupMenu.getMenu().add(service);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem menuItem) {
                        selectedService = menuItem.getTitle().toString();
                        selectService.setText(selectedService);
                        serviceTypeList = retrieveFromDatabase.getListOfServiceTypesByCitiesAndCountryFromDB(countryReference, selectedCountry, selectedCity, selectedService);
                        // String message = getString(R.string.sele)
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
        selectServiceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(NewReservationActivity.this, selectServiceType);

                for (String serviceType : serviceTypeList) {
                    popupMenu.getMenu().add(serviceType);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem menuItem) {
                        selectedServiceType = menuItem.getTitle().toString();
                        selectServiceType.setText(selectedCity);
                        //citiesList = retrieveFromDatabase.getListOfServicesByCitiesAndCountryFromDB(countryReference, selectedCountry, selectedCity);
                        // String message = getString(R.string.sele)
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

    }


    ///prikazi rezultate za odabrane usluge
    ////jos treba pogkledtai u slucaju da se u service i serviceType odabere all

}