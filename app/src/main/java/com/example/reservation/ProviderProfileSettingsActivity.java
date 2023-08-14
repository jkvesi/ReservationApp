package com.example.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.reservation.classes.ReturnServiceClassHolder;
import com.example.reservation.classes.ReturnServicesClass;
import com.example.reservation.classes.ServiceClass;
import com.example.reservation.classes.ServiceSubtypeClass;
import com.example.reservation.classes.UserDataClass;
import com.example.reservation.classes.UserDataHolder;
import com.example.reservation.functions.MapDataToDatabaseClass;
import com.example.reservation.functions.RetrieveDataFromDatabaseClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProviderProfileSettingsActivity extends AppCompatActivity {

    TextView companyNameTextView, serviceTextView, prizeTextView, durationTextView, serviceSubTypetextView, companyNameDisplay;

    RetrieveDataFromDatabaseClass retrieveData;
    FirebaseUser user;
    FirebaseAuth auth;
    String userName;
    Button addNewServicesBtn;
    FrameLayout frameNewService;
    DatabaseReference reference;
    DatabaseReference companyReference;
    List<String> companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_profile_settings);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        companies = new ArrayList<>();
        RetrieveDataFromDatabaseClass retrieveData = new RetrieveDataFromDatabaseClass();

        companyNameTextView = findViewById(R.id.companyNameHolder);
        serviceTextView = findViewById(R.id.serviceTypeHolder);
        serviceSubTypetextView = findViewById(R.id.serviceSubtypeHolder);
        prizeTextView = findViewById(R.id.prizeHolder);
        durationTextView = findViewById(R.id.durationHolder);
        companyNameDisplay = findViewById(R.id.companyDisplay);
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
        /*
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userName).child("Service");

        ReturnServiceClassHolder returnHolder = new ReturnServiceClassHolder();

        List<String> companyNameDisplay = new ArrayList<>();
        List<String> serviceNameDisplay = new ArrayList<>();
        List<String> serviceTypeNameDisplay = new ArrayList<>();

        Query serviceQuery = reference;
        serviceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                Iterable<DataSnapshot> companyNames = snapshot.getChildren();
                for(DataSnapshot companyNameList : companyNames){
                    String companyName = companyNameList.getKey();//profi i susic
                    //za profi uzet programming, za susic farma
                    Iterable<DataSnapshot> servicesNames = companyNameList.getChildren();

                    for(DataSnapshot serviceNameList : servicesNames){
                        String serviceName = serviceNameList.getKey();

                        //za programming ce uzeti java i html
                        Iterable<DataSnapshot> servicesTypes = serviceNameList.getChildren();
                        for(DataSnapshot serviceTypeList : servicesTypes){
                            String serviceType = serviceTypeList.getKey();

                            serviceTypeNameDisplay.add(serviceType);
                        }

                        serviceNameDisplay.add(serviceName);
                    }

                    companyNameDisplay.add(companyName);

                }

            }

            @Override
            public void onCancelled(@NonNull final DatabaseError error) {

            }
        }); */
        //ReturnServicesClass returnServicesClass = new ReturnServicesClass(companyNameDisplay,serviceNameDisplay, serviceTypeNameDisplay);
       // ReturnServiceClassHolder.getInstance().setReturnServices(returnServicesClass);

         //retrieveData.getListOfServices(reference, userData);
       // ReturnServiceClassHolder.getInstance().getReturnServices();
       // ReturnServiceClassHolder.getInstance().getReturnServices().getCompanyList();
        //companies =  retrieveData.getCompanyList(companyReference, UserDataHolder.getInstance().getUserData());

       // companies.size();
        retrieveData.getServices(companyReference, UserDataHolder.getInstance().getUserData());
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
      companyNameDisplay.setText(ReturnServiceClassHolder.getInstance().getReturnServices().getCompanyList().get(1).toString());
    }

}