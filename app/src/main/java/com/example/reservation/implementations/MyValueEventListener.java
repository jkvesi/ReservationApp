package com.example.reservation.implementations;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MyValueEventListener implements ValueEventListener {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
            userName = userSnapshot.child("firstName").getValue(String.class);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // This method is called when the database operation is canceled or fails.
        // You can handle the error or log it here.
    }
}

