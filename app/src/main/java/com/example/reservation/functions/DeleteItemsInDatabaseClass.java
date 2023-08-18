package com.example.reservation.functions;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class DeleteItemsInDatabaseClass {

    public void DeleteServices(DatabaseReference reference){
        //referenca koju dobije ce ici do grada usera

        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DataSnapshot> task) {



            }
        });
    }
}
