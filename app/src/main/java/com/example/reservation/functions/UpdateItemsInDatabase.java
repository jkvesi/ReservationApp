package com.example.reservation.functions;

import com.example.reservation.classes.UserDataClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class UpdateItemsInDatabase {

    public void setTimeSlotAsUnavailable(DatabaseReference reference, String slot, String userName, String date){
        reference.child("Working hours").child(userName).
                child(date).child("timeSlots").child(slot).setValue(false);
    }
}
