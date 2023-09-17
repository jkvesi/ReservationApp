package com.example.reservation;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.example.reservation.classes.SendEmailWorker;

import com.example.reservation.classes.ReminderClass;
import com.example.reservation.classes.SelectedServiceClass;
import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.functions.MapDataToDatabaseClass;
import com.example.reservation.functions.UpdateItemsInDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TimeSlotsFragment extends Fragment{

    DatabaseReference reference;
    ReminderClass reminder = new ReminderClass();
    MapDataToDatabaseClass mapDataToDatabaseClass = new MapDataToDatabaseClass();

    private LinearLayout rowContainer;
    private HashMap<String, Boolean> timeSlotsMap;
    private String pickedDate;

    private String userName;
    SelectedServiceClass selectedService;
    private Dialog gifDialog;


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
               // textView1.setEnabled(false);
                textView1.setBackgroundColor(100);
                showNotesDialog(timeSlots[0], textView1);
                //selectedService.setSlot(timeSlots[0]);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemsInDatabase.setTimeSlotAsUnavailable(reference,timeSlots[1], userName,pickedDate );
              //  textView2.setEnabled(false);
                textView2.setBackgroundColor(100);
                showNotesDialog(timeSlots[1], textView2);
               // selectedService.setSlot(timeSlots[1]);
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemsInDatabase.setTimeSlotAsUnavailable(reference,timeSlots[2], userName,pickedDate );
              //  textView3.setEnabled(false);
                textView3.setBackgroundColor(100);
                showNotesDialog(timeSlots[2], textView3);
            }
        });
        rowContainer.addView(rowView);
    }

    private void showNotesDialog(String timeSlot, TextView text) {

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
                text.setEnabled(false);

                UserDataHolder.getInstance().setPickedSlot(timeSlot);
                int hour = Integer.parseInt(Arrays.stream(timeSlot.split(":")).toList().get(0));
                int minute = Integer.parseInt(Arrays.stream(timeSlot.split(":")).toList().get(1));
                Calendar time = Calendar.getInstance();
                time.set(Calendar.HOUR_OF_DAY, hour); // 24-hour format
                time.set(Calendar.MINUTE, minute);

                LocalDate todays = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d,yyyy");
                String formattedDate = todays.format(formatter);
                LocalDate todaysDate = LocalDate.parse(formattedDate, formatter);
                LocalDate pickedAppointmentDay = LocalDate.parse(pickedDate, DateTimeFormatter.ofPattern("MMM d, yyyy"));

                long daysBetween = ChronoUnit.DAYS.between(todaysDate, pickedAppointmentDay);
                long timeMillis = time.getTimeInMillis();
                long currentTimeMillis = System.currentTimeMillis();
                //postavka slanja maila 24 sata ranije
                long initialDelay = timeMillis - currentTimeMillis + ((daysBetween -1) * 24 * 60 * 60 * 1000);

                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SendEmailWorker.class)
                        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                        .build();

                WorkManager.getInstance(getContext()).enqueue(workRequest);
                String notes = noteEditText.getText().toString();
                dialog.dismiss();
                mapDataToDatabaseClass.saveNotesForPickedSlot(reference, UserDataHolder.getInstance().getSelectedCompanyUserName(),
                        UserDataHolder.getInstance().getPickedDate(),timeSlot, notes, UserDataHolder.getInstance().getUserData().getFirstName(),
                        UserDataHolder.getInstance().getUserData().getLastName());
                mapDataToDatabaseClass.saveReservationsHistory(reference, UserDataHolder.getInstance().getUserData().getFirstName(), UserDataHolder.getInstance().getPickedDate(),
                        UserDataHolder.getInstance().getPickedSlot(),UserDataHolder.getInstance().getSelectedService());
                mapDataToDatabaseClass.saveProvidersAppointemnts(reference,UserDataHolder.getInstance().getSelectedCompanyUserName(),
                       UserDataHolder.getInstance().getUserData(), UserDataHolder.getInstance().getSelectedService(), UserDataHolder.getInstance().getPickedDate(), timeSlot);
                showGifDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissGifDialog();
                    }
                }, 3000);
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

    private void showGifDialog() {
        gifDialog = new Dialog(requireActivity());
        gifDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gifDialog.setContentView(R.layout.dialog_gif);
        gifDialog.setCancelable(false);
        gifDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        gifDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ImageView gifImageView = gifDialog.findViewById(R.id.gifImageView);

        Glide.with(requireContext())
                .asGif()
                .load( R.raw.correct_sign)
                .into(gifImageView);
        gifDialog.show();
    }
    private void dismissGifDialog() {
        if (gifDialog != null && gifDialog.isShowing()) {
            gifDialog.dismiss();
        }

    }
}
