package com.example.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.reservation.classes.DataPickerFragment;
import com.example.reservation.classes.GlobalLists;
import com.example.reservation.classes.SelectedServiceClass;
import com.example.reservation.classes.TimeSlotGenerator;
import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.functions.MapDataToDatabaseClass;
import com.example.reservation.functions.RetrieveDataFromDatabaseClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class NewReservationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    FirebaseDatabase database;
    DatabaseReference countryReference;
    DatabaseReference hourReference;
    DatabaseReference mapWorkingHoursReference;
    DatabaseReference reference;
    DatabaseReference userNameReference;
    List<String> countriesList = new ArrayList<>();
    List<String> citiesList = new ArrayList<>();
    List<String> servicesList = new ArrayList<>();
    List<String> serviceTypeList = new ArrayList<>();
    List<String> companiesResultList = new ArrayList<>();

    List<String> userNameList = new ArrayList<>();

    List<String> workingHours;
    Button displayBtn;

    Button availableAppointmentsBtn;
    String pickedDate;

    Button displaySlotsBtn;
    LinearLayout displaySlots;
    RetrieveDataFromDatabaseClass retrieveDataFromDatabaseClass = new RetrieveDataFromDatabaseClass();
    String selectedCountry, selectedCity, selectedService, selectedServiceType, selectedCompany;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reservation);
        RetrieveDataFromDatabaseClass retrieveDataFromDatabaseClass = new RetrieveDataFromDatabaseClass();

        MapDataToDatabaseClass mapDataToDatabaseClass = new MapDataToDatabaseClass();
        TimeSlotGenerator timeSlotGenerator = new TimeSlotGenerator();
        database = FirebaseDatabase.getInstance();
        countryReference = database.getReference("Countries");
        hourReference = FirebaseDatabase.getInstance().getReference();
        mapWorkingHoursReference = FirebaseDatabase.getInstance().getReference();
        MapDataToDatabaseClass mapToDatabase = new MapDataToDatabaseClass();

        RetrieveDataFromDatabaseClass retrieveFromDatabase = new RetrieveDataFromDatabaseClass();
        RetrieveDataFromDatabaseClass citiesFromDB = new RetrieveDataFromDatabaseClass();
        countriesList = retrieveFromDatabase.getListOfCountriesFromDB(countryReference);

        userNameReference = FirebaseDatabase.getInstance().getReference();
        displaySlots = findViewById(R.id.displaySlots);
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

        displaySlotsBtn = findViewById(R.id.displaySlotsBtn);
        displayBtn = findViewById(R.id.displayTimeSlotsBtn);
        displayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                SelectedServiceClass selectedService = new SelectedServiceClass(selectedServiceType, selectedCompany);
               UserDataHolder.getInstance().setSelectedService(selectedService);
               // userNameList = retrieveDataFromDatabaseClass.getUserNameByCompany(userNameReference, selectedService, selectedServiceType, selectedCompany );
               //check if that node already exist, if not then generate slots and set it to true
                //promjeniti za userName od odabranog pruzatelja usluga, userName od current usera radi jednostavnosti
               hourReference = FirebaseDatabase.getInstance().getReference().child("Working hours").child(userNameList.get(0)).child(pickedDate);
               hourReference.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull final DataSnapshot snapshot) {
                       if(!snapshot.exists()){
                           //ako nije vec postojao dodaj ga u bazi i automatski se moze sve prikazivati jer je sve true
                           HashMap<String, Boolean> timeSlotsMap = timeSlotGenerator.generateTimeSlot(workingHours.get(0), workingHours.get(1), 30);
                           mapToDatabase.saveWorkingHoursToDatabase(userNameList.get(0), mapWorkingHoursReference, pickedDate, workingHours.get(0), workingHours.get(1),timeSlotsMap);
                           TimeSlotsFragment timeSlotsFragment = new TimeSlotsFragment(timeSlotsMap, pickedDate, userNameList.get(0));
                           FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                           fragmentTransaction.replace(R.id.fragmentSlotsContainer, timeSlotsFragment);
                           fragmentTransaction.commit();
                       } else {
                           HashMap<String, Boolean> timeSlotsMap = new HashMap<>();
                           //ako vec postoji u bazi onda ga dohvati sa vrijednostima true ili false
                           for(DataSnapshot slot : snapshot.child("timeSlots").getChildren()){
                             //u listu dodaj samo one slotove koji vec nisu rezervirani
                               if(!slot.child("Notes").exists() || slot.getValue() == (Boolean)true) {
                                 timeSlotsMap.put(slot.getKey(), (Boolean) slot.getValue());
                             }
                           }
                           TimeSlotsFragment timeSlotsFragment = new TimeSlotsFragment(timeSlotsMap, pickedDate,userNameList.get(0));
                           FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                           fragmentTransaction.replace(R.id.fragmentSlotsContainer, timeSlotsFragment);
                           fragmentTransaction.commit();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull final DatabaseError error) {

                   }
               });

            }
        });
        displaySlotsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                UserDataHolder.getInstance().setPickedDate(pickedDate);
                UserDataHolder.getInstance().setSelectedCompanyUserName(userNameList.get(0));
                reference = FirebaseDatabase.getInstance().getReference("Users").child(userNameList.get(0)).child("Working hours").child(pickedDate);
                workingHours =  retrieveDataFromDatabaseClass.getOpenAndCloseHourForPickedDate(reference, userNameList.get(0), pickedDate);
                if( displaySlots.getLayoutParams().height == 0 ) {
                    displaySlots.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    displaySlots.requestLayout();
                } else {
                    displaySlots.getLayoutParams().height = 0;
                    displaySlots.requestLayout();
                }
            }
        });
        availableAppointmentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                userNameList =retrieveDataFromDatabaseClass.getUserNameByCompany(userNameReference, selectedService, selectedServiceType, selectedCompany );
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
                        return false;
                    }
                });

                popupMenu.show();
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