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
import com.example.reservation.classes.GlobalScheduledAppintmentList;
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

public class AppointmentBox extends Fragment {

  Button dismissBtn;
  DatabaseReference reference;

    private Dialog gifDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        reference = FirebaseDatabase.getInstance().getReference();
        DeleteItemsInDatabaseClass deleteItemsInDatabaseClass = new DeleteItemsInDatabaseClass();
        View rootView = inflater.inflate(R.layout.appintments_fragment_box, container, false);

        // Get a reference to the container layout where you want to add the boxes
        LinearLayout containerLayout = rootView.findViewById(R.id.appointmentContainerLayout);

        int listSize = GlobalScheduledAppintmentList.getInstance().getAppointments().size();
        for (int i = 0; i < listSize; i++) {

            View settingsView = inflater.inflate(R.layout.activity_provider_profile_settings, containerLayout, false);

            View boxView = inflater.inflate(R.layout.appointment_box_layout, containerLayout, false);

            dismissBtn = boxView.findViewById(R.id.dismissBtn);

            TextView user = boxView.findViewById(R.id.userHolder);
            TextView service = boxView.findViewById(R.id.serviceHolder);
            TextView slot = boxView.findViewById(R.id.slotHolder);
            TextView date = boxView.findViewById(R.id.dateHolder);

            user.setText(GlobalScheduledAppintmentList.getInstance().getAppointments().get(i).getUser());
            service.setText(GlobalScheduledAppintmentList.getInstance().getAppointments().get(i).getService());
            date.setText(GlobalScheduledAppintmentList.getInstance().getAppointments().get(i).getDate());
            slot.setText(GlobalScheduledAppintmentList.getInstance().getAppointments().get(i).getSlot());

            String companyDelete = GlobalScheduledAppintmentList.getInstance().getAppointments().get(i).getCompany();
            String slotDelete = GlobalScheduledAppintmentList.getInstance().getAppointments().get(i).getSlot();
            String serviceDelete = GlobalScheduledAppintmentList.getInstance().getAppointments().get(i).getService();
            String userDelete = GlobalScheduledAppintmentList.getInstance().getAppointments().get(i).getUser();
            String dateDelete = GlobalScheduledAppintmentList.getInstance().getAppointments().get(i).getDate();
            dismissBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    deleteItemsInDatabaseClass.dismissAppointment(reference,UserDataHolder.getInstance().getUserData(), companyDelete,userDelete, dateDelete, slotDelete );
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
