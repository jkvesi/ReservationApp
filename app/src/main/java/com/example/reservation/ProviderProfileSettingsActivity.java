package com.example.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.reservation.classes.ReturnServiceClassHolder;
import com.example.reservation.classes.ServiceClass;
import com.example.reservation.classes.ServiceSubtypeClass;
import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.functions.MapDataToDatabaseClass;
import com.example.reservation.functions.RetrieveDataFromDatabaseClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProviderProfileSettingsActivity extends AppCompatActivity {

    TextView companyNameTextView, serviceTextView, prizeTextView, durationTextView, serviceSubTypetextView;
    TextView companyNameDisplay, serviceDisplay, serviceTypeDisplay;

    FirebaseUser user;
    FirebaseAuth auth;
    String userName;
    Button addNewServicesBtn;
    FrameLayout frameNewService;
    LinearLayout myServicesLayout;
    List<String> companies;

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


        ImageView iconImageView = findViewById(R.id.homeIcon);
        iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the new activity
                Intent intent = new Intent(ProviderProfileSettingsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        //prikaz servisaaaaaaaaa
        UserDataClass userData = UserDataHolder.getInstance().getUserData();
        userName = userData.getFirstName();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new BoxFragment())
                    .commit();
        }

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

        //kreiraj novu klasu za sve podtipove od te usluge i pohrani je kao podNode od imena servisa
        ServiceSubtypeClass serviceSpec = new ServiceSubtypeClass(serviceType, prize, duration);
        FirebaseDatabase.getInstance().getReference("Users").child(userName).child("Service").child(companyName).child(service).child(serviceType).push().setValue(serviceSpec);

        //morat cemo za sve dodane servise pohranit ih u bazu podataka za odredenu drzavu i grad i pod taj servis
        MapDataToDatabaseClass mapServices = new MapDataToDatabaseClass();
        DatabaseReference serviceReference = FirebaseDatabase.getInstance().getReference().child("Countries");
        mapServices.saveServiceDataToDatabase(userData, serviceReference, serviceData, serviceSpec);

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