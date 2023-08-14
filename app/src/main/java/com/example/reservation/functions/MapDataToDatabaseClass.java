package com.example.reservation.functions;

import androidx.annotation.NonNull;

import com.example.reservation.classes.ServiceClass;
import com.example.reservation.classes.ServiceSubtypeClass;
import com.example.reservation.classes.UserDataClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

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
}
