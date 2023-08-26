package com.example.reservation.functions;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.example.reservation.classes.GlobalLists;
import com.example.reservation.classes.GlobalReservationList;
import com.example.reservation.classes.GlobalScheduledAppintmentList;
import com.example.reservation.classes.Reservation;
import com.example.reservation.classes.ReturnServiceClassHolder;
import com.example.reservation.classes.ReturnServicesClass;
import com.example.reservation.classes.ScheduledAppointmentClass;
import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class RetrieveDataFromDatabaseClass {
    String userName, country, city;

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }


    public List<String> getListOfCountriesFromDB(DatabaseReference reference){
        List<String> countries = new ArrayList<>();
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();

                    for(DataSnapshot country : dataSnapshot.getChildren()){
                        String countryName = country.getKey();
                        countries.add(countryName);
                    }
                }
            }
        });
        return countries;
    }

    public List<String> getListOfCitiesByCountryFromDB(DatabaseReference reference, String country){
        List<String> cities= new ArrayList<>();
        reference.child(country).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();

                    for(DataSnapshot country : dataSnapshot.getChildren()){
                        String countryName = country.getKey();
                        cities.add(countryName);
                    }
                }
            }
        });
        return cities;
    }
    public List<String> getListOfServicesByCitiesAndCountryFromDB(DatabaseReference reference, String country, String city){
        List<String> services= new ArrayList<>();
        reference.child(country).child(city).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();

                    for(DataSnapshot country : dataSnapshot.getChildren()){
                        String countryName = country.getKey();
                        services.add(countryName);
                    }
                }
            }
        });
        return services;
    }
    public List<String> getListOfServiceTypesByCitiesAndCountryFromDB(DatabaseReference reference, String country, String city, String service){
        List<String> serviceTypes = new ArrayList<>();
        reference.child(country).child(city).child(service).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();

                    for(DataSnapshot country : dataSnapshot.getChildren()){
                        String countryName = country.getKey();
                        serviceTypes.add(countryName);
                    }
                }
            }
        });
        return serviceTypes;
    }

    public List<String> getListOfCompaniesForSelectedService(DatabaseReference reference, String country, String city, String service, String serviceType){
        List<String> companies = new ArrayList<>();
        reference.child(country).child(city).child(service).child(serviceType).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();

                    for(DataSnapshot companyName : dataSnapshot.getChildren()){
                        String company = companyName.getKey();
                        companies.add(company);
                    }
                }
            }
        });
        return companies;
    }

    public void getServices (DatabaseReference databaseReference, UserDataClass user) {
        List<String> companies = new ArrayList<>();
        List<String> services = new ArrayList<>();
        List<String> serviceTypes = new ArrayList<>();
        GlobalLists.getInstance().getMasterList().clear();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getFirstName()).child("Service");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot company : dataSnapshot.getChildren()) {
                            String companyName = company.getKey();
                            for (DataSnapshot service : company.getChildren()) {
                                String serviceName = service.getKey();
                                for (DataSnapshot serviceType : service.getChildren()) {
                                    String serviceTypeName = serviceType.getKey();
                                    serviceTypes.add(serviceTypeName);
                                    List<String> list = new ArrayList<>();
                                    list.add(companyName);
                                    list.add(serviceName);
                                    list.add(serviceTypeName);

                                    GlobalLists.getInstance().addList(list);
                                }
                                services.add(serviceName);
                            }
                            companies.add(companyName);
                        }

                    }
                    ReturnServicesClass returnServicesClass = new ReturnServicesClass(companies, services, serviceTypes);
                    ReturnServiceClassHolder.getInstance().setReturnServices(returnServicesClass);
                }
        });

    }
    public List<String> getOpenAndCloseHourForPickedDate( DatabaseReference reference,String user, String date){
        //reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getFirstName());

        List<String> workingHoursList = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String openingHour = dataSnapshot.child("openHour").getValue(String.class);
                String closingHour = dataSnapshot.child("closeHour").getValue(String.class);
                workingHoursList.add(openingHour);
                workingHoursList.add(closingHour);
                // Now you have the opening and closing hours for the specified date
               // Log.d("Hours", "Opening Hour: " + openingHour + ", Closing Hour: " + closingHour);
            }

            @Override
            public void onCancelled(DatabaseError error) {
              //  Log.w("FirebaseError", "Failed to read value.", error.toException());
            }
        });
        return workingHoursList;
    }

    public HashMap<String, Boolean> getTimeSlotsForSelectedDate(DatabaseReference reference, String pickedDate){
        HashMap<String, Boolean> timeSlots = new HashMap<>();
        reference.child("timeSlots").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                    for(DataSnapshot country : snapshot.getChildren()){
                        timeSlots.put(country.getKey(), (Boolean) country.getValue());
                    }
            }

            @Override
            public void onCancelled(@NonNull final DatabaseError error) {

            }
        });
        return timeSlots;
    }

    public List<String> getUserNameByCompany (DatabaseReference reference, String service, String serviceType, String company){
       List<String> userNameList = new ArrayList<>();
        reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userName = userSnapshot.getKey();

                    if(dataSnapshot.child(userName).child("Service").child(company).child(service).child(serviceType).exists()){
                        userNameList.add(userName);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
       return userNameList;
    }

    public void getPreviousReservations(DatabaseReference reference, String user) {

        if(!(GlobalReservationList.getInstance().getReservations() == null)) {
            GlobalReservationList.getInstance().getReservations().clear();
        }
        reference.child("Users").child(user).child("My reservations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot reservationSnapshot : dataSnapshot.getChildren()) {
                    String date = reservationSnapshot.getKey();
                    for (DataSnapshot slotSnapshot : reservationSnapshot.getChildren()) {
                        String slot = slotSnapshot.getKey();
                        String company = slotSnapshot.child("company").getValue(String.class);
                        String service = slotSnapshot.child("service").getValue(String.class);
                        String provider = slotSnapshot.child("provider").getValue(String.class);

                        // Create a Reservation object and add it to the list
                        Reservation reservation = new Reservation(provider, date, slot, company, service);
                        GlobalReservationList.getInstance().addReservation(reservation);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    public void getScheduledAppointments(DatabaseReference reference, String currentUser) {

        if(!(GlobalScheduledAppintmentList.getInstance().getAppointments() == null)) {
            GlobalScheduledAppintmentList.getInstance().getAppointments().clear();
        }
        reference.child("Users").child(currentUser).child("Appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot companySnapshot : dataSnapshot.getChildren()) {
                    String company = companySnapshot.getKey();
                    for (DataSnapshot userSnapshot : companySnapshot.getChildren()) {
                        String user = userSnapshot.getKey();
                        for (DataSnapshot dateSnapshot : userSnapshot.getChildren()) {
                            String date = dateSnapshot.getKey();
                            for(DataSnapshot slotSnapshot : dateSnapshot.getChildren()) {
                                String slot = slotSnapshot.getKey();
                                String service = slotSnapshot.child("service").getValue(String.class);

                                ScheduledAppointmentClass scheduledAppointmentClass = new ScheduledAppointmentClass(user, date,slot,company,service);
                                GlobalScheduledAppintmentList.getInstance().addAppointment(scheduledAppointmentClass);
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
}
