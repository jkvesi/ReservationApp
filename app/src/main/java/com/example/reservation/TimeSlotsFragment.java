package com.example.reservation;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.reservation.classes.SelectedServiceClass;
import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.functions.MapDataToDatabaseClass;
import com.example.reservation.functions.RetrieveDataFromDatabaseClass;
import com.example.reservation.functions.UpdateItemsInDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TimeSlotsFragment extends Fragment{

    DatabaseReference reference;
    MapDataToDatabaseClass mapDataToDatabaseClass = new MapDataToDatabaseClass();

    private LinearLayout rowContainer;
    private HashMap<String, Boolean> timeSlotsMap;
    private String pickedDate;

    private String userName;
    SelectedServiceClass selectedService;


    public TimeSlotsFragment(final HashMap<String, Boolean> timeSlotsMap, String pickedDate, String userName) {
        this.timeSlotsMap = timeSlotsMap;
        this.pickedDate = pickedDate;
        this.userName = userName;
    }

    public HashMap<String, Boolean> getTimeSlotsMap() {
        return timeSlotsMap;
    }

    public void setTimeSlotsMap(final HashMap<String, Boolean> timeSlotsMap) {
        this.timeSlotsMap = timeSlotsMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_slots, container, false);

        reference = FirebaseDatabase.getInstance().getReference();
        rowContainer = rootView.findViewById(R.id.containerSlotsContainer);
        int itemsPerRow = 3;
        int count = 0;
        String[] rowItems = new String[itemsPerRow];
        for (HashMap.Entry<String, Boolean> entry : timeSlotsMap.entrySet()) {
            rowItems[count % itemsPerRow] = entry.getKey();
            count++;

            if (count % itemsPerRow == 0) {
                addTimeSlotRow(rowItems);
                rowItems = new String[itemsPerRow];
            }
        }

        if (count % itemsPerRow != 0) {
            addTimeSlotRow(rowItems);
        }

        return rootView;
    }

    private void addTimeSlotRow(String[] timeSlots) {
        View rowView = LayoutInflater.from(requireContext()).inflate(R.layout.row_time_slot, rowContainer, false);

        UpdateItemsInDatabase updateItemsInDatabase = new UpdateItemsInDatabase();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        TextView textView1 = rowView.findViewById(R.id.textView1);
        TextView textView2 = rowView.findViewById(R.id.textView2);
        TextView textView3 = rowView.findViewById(R.id.textView3);

        // Fill TextViews with time slot data
        textView1.setText(timeSlots[0]);
        textView2.setText(timeSlots[1]);
        textView3.setText(timeSlots[2]);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemsInDatabase.setTimeSlotAsUnavailable(reference, timeSlots[0], userName,pickedDate );
                textView1.setEnabled(false);
                textView1.setBackgroundColor(100);
                showNotesDialog(timeSlots[0]);
                //selectedService.setSlot(timeSlots[0]);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemsInDatabase.setTimeSlotAsUnavailable(reference,timeSlots[1], userName,pickedDate );
                textView2.setEnabled(false);
                textView2.setBackgroundColor(100);
                showNotesDialog(timeSlots[1]);
               // selectedService.setSlot(timeSlots[1]);
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemsInDatabase.setTimeSlotAsUnavailable(reference,timeSlots[2], userName,pickedDate );
                textView3.setEnabled(false);
                textView3.setBackgroundColor(100);
                showNotesDialog(timeSlots[2]);
               // selectedService.setSlot(timeSlots[2]);
            }
        });
        rowContainer.addView(rowView);
    }

    private void showNotesDialog(String timeSlot) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.reservation_notes, null);
        builder.setView(dialogView);

        EditText noteEditText = dialogView.findViewById(R.id.noteEditText);
        Button saveButton = dialogView.findViewById(R.id.saveButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDataHolder.getInstance().setPickedSlot(timeSlot);
                String notes = noteEditText.getText().toString();
                dialog.dismiss();
                if(notes == null){
                    notes = "Nothing to highlight!";
                }
                mapDataToDatabaseClass.saveNotesForPickedSlot(reference, UserDataHolder.getInstance().getSelectedCompanyUserName(),
                        UserDataHolder.getInstance().getPickedDate(),timeSlot, notes, UserDataHolder.getInstance().getUserData().getFirstName(),
                        UserDataHolder.getInstance().getUserData().getLastName());
                mapDataToDatabaseClass.saveReservationsHistory(reference, UserDataHolder.getInstance().getUserData().getFirstName(), UserDataHolder.getInstance().getPickedDate(),
                        UserDataHolder.getInstance().getPickedSlot(),UserDataHolder.getInstance().getSelectedService());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
