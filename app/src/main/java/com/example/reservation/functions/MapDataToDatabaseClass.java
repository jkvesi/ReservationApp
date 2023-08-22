package com.example.reservation.functions;

import androidx.annotation.NonNull;

import com.example.reservation.classes.SelectedServiceClass;
import com.example.reservation.classes.ServiceClass;
import com.example.reservation.classes.ServiceSubtypeClass;
import com.example.reservation.classes.UserDataClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;

public class MapDataToDatabaseClass {

    public void mapCountryAndCityToDatabase(String country, String city, DatabaseReference reference){
        reference.child(country).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot countrySnapshot = task.getResult();
                    if(countrySnapshot.exists()){
                        if(!countrySnapshot.child(city).exists()){
                            reference.child(country).child(city).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull final Task<Void> task) {

                                }
                            });
                        }
                    } else {
                        reference.child(country).child(city).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull final Task<Void> task) {

                            }
                        });
                    }
                }
            }
        });
    }

    public void saveServiceDataToDatabase(UserDataClass user, DatabaseReference reference, ServiceClass serviceClass, ServiceSubtypeClass serviceSubtypeClass) {
        DatabaseReference serviceRef = reference.child(user.getCountry()).child(user.getCity());
        serviceRef.child(serviceClass.getService()).child(serviceSubtypeClass.getSubtype()).child(serviceClass.getCompanyName()).setValue(true);
    }

    public void saveWorkingHoursToDatabase(String userName, DatabaseReference reference, String date, String openingHour, String closingHour, HashMap<String, Boolean> timeSlots){
        reference.child("Working hours").child(userName).child(date).child("openHour").setValue(openingHour);
        reference.child("Working hours").child(userName).child(date).child("closeHour").setValue(closingHour);
        reference.child("Working hours").child(userName).child(date).child("timeSlots").setValue(timeSlots);
    }
    public void saveNotesForPickedSlot( DatabaseReference reference,String userName, String date, String slot, String notes, String userFirstName, String userLastName ){
        reference.child("Working hours").child(userName).child(date).child("timeSlots").child(slot).child("Notes").setValue(userFirstName + " " + userLastName + ":" + notes);
    }

    public void saveReservationsHistory(DatabaseReference reference, String userName, String pickedDate, String pickedSlot, SelectedServiceClass selectedService){
        reference.child("Users").child(userName).child("My reservations").child(pickedDate).child(pickedSlot).setValue(selectedService);
    }
}
