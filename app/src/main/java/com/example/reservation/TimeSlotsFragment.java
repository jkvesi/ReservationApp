package com.example.reservation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.functions.UpdateItemsInDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TimeSlotsFragment extends Fragment {

    private LinearLayout rowContainer;
    private HashMap<String, Boolean> timeSlotsMap;
    private String pickedDate;

    private String userName;


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

        rowContainer = rootView.findViewById(R.id.containerSlotsContainer);



        // Populate rows
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

        // Add the last row if needed
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
                updateItemsInDatabase.setTimeSlotAsUnavailable(reference,timeSlots[0], userName,pickedDate );
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemsInDatabase.setTimeSlotAsUnavailable(reference,timeSlots[1], userName,pickedDate );
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemsInDatabase.setTimeSlotAsUnavailable(reference,timeSlots[2], userName,pickedDate );
            }
        });
        rowContainer.addView(rowView);
    }

}
