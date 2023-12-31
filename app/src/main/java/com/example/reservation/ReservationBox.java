package com.example.reservation;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.reservation.classes.GlobalLists;
import com.example.reservation.classes.GlobalReservationList;
import com.example.reservation.classes.ReturnServiceClassHolder;
import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.functions.DeleteItemsInDatabaseClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReservationBox extends Fragment {

    Button cancleReservationBtn;
    Date pickedDate;
    Date currentDate;
    DatabaseReference reference;
    Dialog gifDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        DeleteItemsInDatabaseClass deleteItemsInDatabaseClass = new DeleteItemsInDatabaseClass();
        reference = FirebaseDatabase.getInstance().getReference();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String currentDateFormatted = dateFormat.format(new Date());
        View rootView = inflater.inflate(R.layout.reservation_fragment_box, container, false);


        // Get a reference to the container layout where you want to add the boxes
        LinearLayout containerLayout = rootView.findViewById(R.id.reservationContainerLayout);

        int listSize = GlobalReservationList.getInstance().getReservations().size();
        for (int i = 0; i < listSize; i++) {
            try {
                currentDate = dateFormat.parse(currentDateFormatted);
                pickedDate = dateFormat.parse(GlobalReservationList.getInstance().getReservations().get(i).getDate());
            } catch (ParseException e){
                e.printStackTrace();
            }

            int comparisonResult = pickedDate.compareTo(currentDate);

            View settingsView = inflater.inflate(R.layout.activity_previous_reservation, containerLayout, false);

            View boxView = inflater.inflate(R.layout.reservation_box_layout, containerLayout, false);

            cancleReservationBtn = boxView.findViewById(R.id.cancelReservationBtn);
            if (comparisonResult > 0) {
                cancleReservationBtn.setEnabled(true);

            } else if (comparisonResult < 0) {
                cancleReservationBtn.setEnabled(false);

            } else {
                cancleReservationBtn.setEnabled(false);

            }
            TextView company= boxView.findViewById(R.id.companyHolder);
            TextView serviceType = boxView.findViewById(R.id.serviceTypeHolder);
            TextView slot = boxView.findViewById(R.id.slotHolder);
            TextView date = boxView.findViewById(R.id.dateHolder);

            company.setText(GlobalReservationList.getInstance().getReservations().get(i).getCompany());
            serviceType.setText(GlobalReservationList.getInstance().getReservations().get(i).getService());
            date.setText(GlobalReservationList.getInstance().getReservations().get(i).getDate());
            slot.setText(GlobalReservationList.getInstance().getReservations().get(i).getSlot());

            String userName = GlobalReservationList.getInstance().getReservations().get(i).getUser();
            String pickedDate = GlobalReservationList.getInstance().getReservations().get(i).getDate();
            String pickedSlot = GlobalReservationList.getInstance().getReservations().get(i).getSlot();

            String companyName = GlobalReservationList.getInstance().getReservations().get(i).getCompany();
            cancleReservationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    deleteItemsInDatabaseClass.deleteNotesAndSetSlotAsFree(reference, pickedSlot, pickedDate, userName, UserDataHolder.getInstance().getUserData());
                    String user = UserDataHolder.getInstance().getUserData().getFirstName() + " " + UserDataHolder.getInstance().getUserData().getLastName();
                    reference.child("Users").child(userName).
                            child("Appointments").child(companyName).child(user).child(pickedDate).child(pickedSlot).removeValue();


                    showGifDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            containerLayout.removeView(boxView);
                            dismissGifDialog();
                        }
                    }, 2000); // Duration in milliseconds (2 seconds)
                }


            });
            containerLayout.addView(boxView);
        }

        return rootView;
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
                .load( R.raw.cancle)
                .into(gifImageView);

        gifDialog.show();
    }
    private void dismissGifDialog() {
        if (gifDialog != null && gifDialog.isShowing()) {
            gifDialog.dismiss();
        }
    }
}
