package com.example.reservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;

import android.app.DatePickerDialog;
import androidx.fragment.app.FragmentTransaction;
import android.app.TimePickerDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reservation.classes.DataPickerFragment;
import com.example.reservation.classes.GlobalLists;
import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.functions.MapDataToDatabaseClass;
import com.example.reservation.functions.RetrieveDataFromDatabaseClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

public class NewReservationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Spinner countrySpinner, citySpinner;
    TextView comapnies;
    FirebaseDatabase database;
    DatabaseReference countryReference;
    DatabaseReference hourReference;
    DatabaseReference reference;
    List<String> countriesList = new ArrayList<>();
    List<String> citiesList = new ArrayList<>();
    List<String> servicesList = new ArrayList<>();
    List<String> serviceTypeList = new ArrayList<>();
    List<String> companiesResultList = new ArrayList<>();

    List<String> workingHours;
    TextView displayHour;
    Button displayBtn;

    Button availableAppointmentsBtn;
    String pickedDate;
    Button privremeni;
    String selectedCountry, selectedCity, selectedService, selectedServiceType, selectedCompany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reservation);
        privremeni = findViewById(R.id.privremeniBtn);
        RetrieveDataFromDatabaseClass retrieveDataFromDatabaseClass = new RetrieveDataFromDatabaseClass();


        database = FirebaseDatabase.getInstance();
        countryReference = database.getReference("Countries");
        hourReference = FirebaseDatabase.getInstance().getReference();
        MapDataToDatabaseClass mapToDatabase = new MapDataToDatabaseClass();

        RetrieveDataFromDatabaseClass retrieveFromDatabase = new RetrieveDataFromDatabaseClass();
        RetrieveDataFromDatabaseClass citiesFromDB = new RetrieveDataFromDatabaseClass();
        countriesList = retrieveFromDatabase.getListOfCountriesFromDB(countryReference);

        availableAppointmentsBtn = findViewById(R.id.availableAppointmentsBtn);
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
        TextView selectCompany = findViewById(R.id.companiesSelector);

        displayBtn = findViewById(R.id.displayBtn);
        displayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                privremeni.setText(workingHours.get(0));

                mapToDatabase.saveWorkingHoursToDatabase(UserDataHolder.getInstance().getUserData(), hourReference, pickedDate, workingHours.get(0), workingHours.get(1) );
            }
        });
        availableAppointmentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DialogFragment datePicker = new DataPickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });

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
                        selectServiceType.setText(selectedServiceType);
                        companiesResultList = retrieveFromDatabase.getListOfCompaniesForSelectedService(countryReference, selectedCountry, selectedCity, selectedService, selectedServiceType);
                        // countries.setText(companiesResultList.get(0));
                        // GlobalLists.getInstance().setCompaniesList(companiesResultList);
                        // String message = getString(R.string.sele)
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
        ///
        selectCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(NewReservationActivity.this, selectCompany);

                for (String company : companiesResultList) {
                    popupMenu.getMenu().add(company);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem menuItem) {
                        selectedCompany = menuItem.getTitle().toString();
                        selectCompany.setText(selectedCompany);
                        // companiesResultList = retrieveFromDatabase.getListOfCompaniesForSelectedService(countryReference, selectedCountry, selectedCity, selectedService, selectedServiceType);
                        // countries.setText(companiesResultList.get(0));
                        // GlobalLists.getInstance().setCompaniesList(companiesResultList);
                        // String message = getString(R.string.sele)
                        return false;
                    }
                });

                popupMenu.show();
            }
        });

        privremeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                reference = FirebaseDatabase.getInstance().getReference("Users").child(UserDataHolder.getInstance().getUserData().getFirstName()).child("Service").child("Working hours").child(pickedDate);
                workingHours =  retrieveDataFromDatabaseClass.getOpenAndCloseHourForPickedDate(reference, UserDataHolder.getInstance().getUserData(), pickedDate);

              //  mapToDatabase.saveWorkingHoursToDatabase(UserDataHolder.getInstance().getUserData(), hourReference, pickedDate, workingHours.get(0), workingHours.get(1) );

            }
        });

    }
    @Override
    public void onDateSet(final DatePicker datePicker, final int year, final int month, final int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        pickedDate = DateFormat.getDateInstance().format(calendar.getTime());
    }



}