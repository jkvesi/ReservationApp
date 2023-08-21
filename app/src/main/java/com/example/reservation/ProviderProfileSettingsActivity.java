package com.example.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import androidx.fragment.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.example.reservation.classes.DataPickerFragment;
import com.example.reservation.classes.GlobalLists;
import com.example.reservation.classes.ReturnServiceClassHolder;
import com.example.reservation.classes.ServiceClass;
import com.example.reservation.classes.ServiceSubtypeClass;
import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.classes.WorkingHoursClass;
import com.example.reservation.functions.MapDataToDatabaseClass;
import com.example.reservation.functions.RetrieveDataFromDatabaseClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderProfileSettingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    TextView companyNameTextView, serviceTextView, prizeTextView, durationTextView, serviceSubTypetextView;
    TextView companyNameDisplay, serviceDisplay, serviceTypeDisplay;

    FirebaseUser user;
    FirebaseAuth auth;
    String userName;
    Button addNewServicesBtn, myWorkingHoursBtn;
    FrameLayout frameNewService, frameWorkingHours;
    LinearLayout myServicesLayout;
    List<String> companies;
    Button selectDayBtn, openingHourBtn, closingHourBtn;
    int hourOpen, minuteOpen;
    int hourClose, minuteClose;
    TextView dateDisplay, closeHourDisplay, openHourDisplay;
    DatabaseReference companyReference;
    Button myServicesBtn;
    private BoxFragment boxFragment;
    TextView toolBarTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_profile_settings);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        companies = new ArrayList<>();

        myServicesLayout = findViewById(R.id.fragmentContainer);
        companyNameTextView = findViewById(R.id.companyNameHolder);
        serviceTextView = findViewById(R.id.serviceTypeHolder);
        serviceSubTypetextView = findViewById(R.id.serviceSubtypeHolder);
        prizeTextView = findViewById(R.id.prizeHolder);
        durationTextView = findViewById(R.id.durationHolder);
        addNewServicesBtn = findViewById(R.id.addServicesBtn);
        frameNewService = findViewById(R.id.addNewServiceFrame);
        selectDayBtn = findViewById(R.id.selectDayBtn);
        openingHourBtn = findViewById(R.id.selectOpenHourBtn);
        closingHourBtn = findViewById(R.id.selectClosHourBtn);
        myWorkingHoursBtn = findViewById(R.id.workingHoursBtn);
        frameWorkingHours = findViewById(R.id.workingHoursFrame);
        myServicesBtn = findViewById(R.id.myServicesBtn);
        boxFragment = new BoxFragment();
        toolBarTextView = findViewById(R.id.toolbarText);

        ImageView iconImageView = findViewById(R.id.homeIcon);
        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the new activity
                Intent intent = new Intent(ProviderProfileSettingsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        selectDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DialogFragment datePicker = new DataPickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        openingHourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(final TimePicker timePicker, final int selectedHour, final int selectedMinute) {
                        hourOpen = selectedHour;
                        minuteOpen = selectedMinute;
                        String display = String.format(Locale.getDefault(),"%02d:%02d", hourOpen, minuteOpen);
                        openHourDisplay = findViewById(R.id.openingHourTextView);
                        openHourDisplay.setText(display);
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(ProviderProfileSettingsActivity.this, onTimeSetListener, hourOpen, minuteOpen, true);

                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        closingHourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(final TimePicker timePicker, final int selectedHour, final int selectedMinute) {
                        hourClose = selectedHour;
                        minuteClose = selectedMinute;
                        String display = String.format(Locale.getDefault(),"%02d:%02d", hourClose, minuteClose);
                        closeHourDisplay = findViewById(R.id.closingHourTextView);
                        if(hourClose < hourOpen || (hourOpen == hourClose && minuteClose < minuteOpen) ){
                            Toast.makeText(ProviderProfileSettingsActivity.this, "Closing must be after opening!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            closeHourDisplay.setText(display);
                        }
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(ProviderProfileSettingsActivity.this, onTimeSetListener, hourClose, minuteClose, true);

                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        //prikaz servisaaaaaaaaa
        UserDataClass userData = UserDataHolder.getInstance().getUserData();
        userName = userData.getFirstName();

        toolBarTextView.setText(userData.getFirstName() + " " + userData.getLastName());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new BoxFragment())
                    .commit();
        }

    }

    @Override
    public void onDateSet(final DatePicker datePicker, final int year, final int month, final int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        String pickedDate = DateFormat.getDateInstance().format(calendar.getTime());

        dateDisplay = findViewById(R.id.dateTextView);
        dateDisplay.setText(pickedDate);
    }

    public void onAddServicesBtnClick(View v){
        if( frameNewService.getLayoutParams().height == 0 ) {
            frameNewService.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            frameNewService.requestLayout();
        } else {
            frameNewService.getLayoutParams().height = 0;
            frameNewService.requestLayout();
        }
    }

    public void onSaveButtonClick(View v){
        String companyName, service, serviceType;
        int prize;
        double duration;
        companyName = companyNameTextView.getText().toString();
        service = serviceTextView.getText().toString();
        serviceType = serviceSubTypetextView.getText().toString();
        prize = Integer.parseInt(prizeTextView.getText().toString());
        duration = Double.parseDouble(durationTextView.getText().toString());

        //dohvati ime usera, zemlju i grad //izdvoji u zasebnu klasu
        UserDataClass userData = UserDataHolder.getInstance().getUserData();
        userName = userData.getFirstName();

        //kreiraj novu klasu za ServiceClass i spremi je u DB pod dobiveni userName
        ServiceClass serviceData = new ServiceClass(companyName, service);
        FirebaseDatabase.getInstance().getReference("Users").child(userName).child("Service").child(companyName);
        List<String> newAddedService = new ArrayList<>();
        newAddedService.add(companyName);
        newAddedService.add(service);
        newAddedService.add(serviceType);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new NewAddedServiceFragment(companyName,service,serviceType))
                .commit();
        GlobalLists.getInstance().getMasterList().add(newAddedService);

        companyNameTextView.setText("");
        serviceTextView.setText("");
        serviceSubTypetextView.setText("");
        prizeTextView.setText("");
        durationTextView.setText("");
        //kreiraj novu klasu za sve podtipove od te usluge i pohrani je kao podNode od imena servisa
        ServiceSubtypeClass serviceSpec = new ServiceSubtypeClass(serviceType, prize, duration);
        FirebaseDatabase.getInstance().getReference("Users").child(userName).child("Service").child(companyName).child(service).child(serviceType).push().setValue(serviceSpec);

        //morat cemo za sve dodane servise pohranit ih u bazu podataka za odredenu drzavu i grad i pod taj servis
        MapDataToDatabaseClass mapServices = new MapDataToDatabaseClass();
        DatabaseReference serviceReference = FirebaseDatabase.getInstance().getReference().child("Countries");
        mapServices.saveServiceDataToDatabase(userData, serviceReference, serviceData, serviceSpec);

        RetrieveDataFromDatabaseClass retrieveData = new RetrieveDataFromDatabaseClass();
        retrieveData.getServices(companyReference, UserDataHolder.getInstance().getUserData());

    }
    public void onSaveAndAddTimeBtnClick(View v){
        String date = dateDisplay.getText().toString();
        String openHour = openHourDisplay.getText().toString();
        String closeHour = closeHourDisplay.getText().toString();

        WorkingHoursClass workingHours = new WorkingHoursClass(date, openHour, closeHour);
        FirebaseDatabase.getInstance().getReference("Users").child(userName).child("Working hours").child(date).setValue(workingHours);

        dateDisplay.setText("");
        openHourDisplay.setText("");
        closeHourDisplay.setText("");
    }

    public void onMyWorkingHoursBtnClick(View v){
        if( frameWorkingHours.getLayoutParams().height == 0 ) {
            frameWorkingHours.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            frameWorkingHours.requestLayout();
        } else {
            frameWorkingHours.getLayoutParams().height = 0;
            frameWorkingHours.requestLayout();
        }
    }
    public void onMyServicesClick(View v){

        if( myServicesLayout.getLayoutParams().height == 0 ) {
            myServicesLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            myServicesLayout.requestLayout();
        } else {
            myServicesLayout.getLayoutParams().height = 0;
            myServicesLayout.requestLayout();
        }
    }
}