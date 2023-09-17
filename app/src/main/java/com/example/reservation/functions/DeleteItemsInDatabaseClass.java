package com.example.reservation.functions;

import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteItemsInDatabaseClass {

    public void DeleteServices(DatabaseReference reference, String service, String serviceType, String company, UserDataClass user){
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(final Void unused) {

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Countries").child(user.getCountry()).child(user.getCity()).
                                child(service).child(serviceType).child(company);

                        DatabaseReference referenceToDelete = databaseReference.getParent();
                        referenceToDelete.removeValue();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull final Exception e) {
                    }
                });
    }

    public void deleteNotesAndSetSlotAsFree(DatabaseReference reference, String slot, String date, String userName, UserDataClass user){
         reference.child("Working hours").child(userName).child(date).
                child("timeSlots").child(slot).child("Notes").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(final Void unused) {
                reference.child("Working hours").child(userName).child(date).
                        child("timeSlots").child(slot).setValue(true);
                //delete reservations from My reservations History
                reference.child("Users").child(user.getFirstName()).
                        child("My reservations").child(date).child(slot).removeValue();
            }
        });
    }
    public void dismissAppointment(DatabaseReference reference, UserDataClass currentUser, String company, String userName, String date, String slot){
        reference.child("Users").child(currentUser.getFirstName()).
                child("Appointments").child(company).child(userName).child(date).child(slot).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(final Void unused) {
                        reference.child("Working hours").child(currentUser.getFirstName()).child(date).
                                child("timeSlots").child(slot).setValue(true);
                        reference.child("Users").child(currentUser.getFirstName()).
                                child("My reservations").child(date).child(slot).removeValue();
                    }
                });
    }
}
