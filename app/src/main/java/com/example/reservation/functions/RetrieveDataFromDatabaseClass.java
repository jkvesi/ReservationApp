package com.example.reservation.functions;

import androidx.annotation.NonNull;

import com.example.reservation.classes.GlobalLists;
import com.example.reservation.classes.ReturnServiceClassHolder;
import com.example.reservation.classes.ReturnServicesClass;
import com.example.reservation.classes.UserDataClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetrieveDataFromDatabaseClass {
    String userName, country, city;

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public UserDataClass getUserName(FirebaseUser user, DatabaseReference reference){

        UserDataClass userClass = new UserDataClass();
        String userEmail = user.getEmail();

        reference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    userClass.setFirstName(userSnapshot.child("firstName").getValue(String.class));
                    userClass.setCountry(userSnapshot.child("country").getValue(String.class));
                    userClass.setCity(userSnapshot.child("city").getValue(String.class));
                    userClass.setLastName(userSnapshot.child("lastName").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull final DatabaseError error) {

            }
        });
        return userClass;
    }

    public List<String> getListOfCountriesFromDB(DatabaseReference reference){
        List<String> countries = new ArrayList<>();
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();

                    for(DataSnapshot country : dataSnapshot.getChildren()){
                        String countryName = country.getKey();
                        countries.add(countryName);
                    }
                }
            }
        });
        return countries;
    }

    public List<String> getListOfCities(DatabaseReference reference, String country){
        List<String> cities = new ArrayList<>();

        reference.child(country).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();

                    for(DataSnapshot country : dataSnapshot.getChildren()){
                        String countryName = country.getKey();
                        cities.add(countryName);
                    }
                }
            }
        });
        return cities;
    }

    public void getListOfServices(DatabaseReference databaseReference, UserDataClass user ){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getFirstName()).child("Service");

        ReturnServiceClassHolder returnHolder = new ReturnServiceClassHolder();

        List<String> companyNameDisplay = new ArrayList<>();
        List<String> serviceNameDisplay = new ArrayList<>();
        List<String> serviceTypeNameDisplay = new ArrayList<>();

        Query serviceQuery = databaseReference;
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

                            returnHolder.getReturnServices().getServiceTypeList().add(serviceType);
                        }

                        //serviceNameDisplay.add(serviceName);
                        returnHolder.getReturnServices().getServiceList().add(serviceName);
                    }

                   // companyNameDisplay.add(companyName);

                    returnHolder.getReturnServices().getCompanyList().add(companyName);

                }
              /*  returnService.setServiceTypeList(serviceTypeNameDisplay);
                returnService.setServiceList(serviceNameDisplay);
                returnService.setCompanyList(companyNameDisplay);*/


            }

            @Override
            public void onCancelled(@NonNull final DatabaseError error) {

            }
        });

    }

    public List<String> getCompanyNames(DatabaseReference databaseReference, UserDataClass user){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getFirstName()).child("Service");

        List<String> companyNameDisplay = new ArrayList<>();

        Query serviceQuery = databaseReference;
        serviceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                Iterable<DataSnapshot> companyNames = snapshot.getChildren();
                for(DataSnapshot companyNameList : companyNames){
                    String companyName = companyNameList.getKey();//profi i susic
                    //za profi uzet programming, za susic farma
                    Iterable<DataSnapshot> servicesNames = companyNameList.getChildren();
                    companyNameDisplay.add(companyName);
                }
            }
            @Override
            public void onCancelled(@NonNull final DatabaseError error) {

            }
        });

        return companyNameDisplay;
    }

    public void getServices (DatabaseReference databaseReference, UserDataClass user) {
        List<String> companies = new ArrayList<>();
        List<String> services = new ArrayList<>();
        List<String> serviceTypes = new ArrayList<>();
        GlobalLists.getInstance().getMasterList().clear();
        String display = "";
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getFirstName()).child("Service");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    for (DataSnapshot company : dataSnapshot.getChildren()) {
                        String companyName = company.getKey();
                        for (DataSnapshot service : company.getChildren()) {
                            String serviceName = service.getKey();
                            for (DataSnapshot serviceType : service.getChildren()) {
                                String serviceTypeName = serviceType.getKey();
                                serviceTypes.add(serviceTypeName);
                                List<String> list = new ArrayList<>();
                                list.add(companyName);
                                list.add(serviceName);
                                list.add(serviceTypeName);

                                GlobalLists.getInstance().addList(list);
                            }
                            services.add(serviceName);
                        }
                        companies.add(companyName);

                    }
                    ReturnServicesClass returnServicesClass = new ReturnServicesClass(companies, services, serviceTypes);
                    ReturnServiceClassHolder.getInstance().setReturnServices(returnServicesClass);
                }
            }
        });

    }
    public List<List<String>> presentDataServices (ReturnServicesClass returnServices){
        List<String> returnedList = new ArrayList<>();

        List<List<String>> displayList = new ArrayList<>();

        int numOfCompanies = ReturnServiceClassHolder.getInstance().getReturnServices().getCompanyList().size();
        int numOfServices = ReturnServiceClassHolder.getInstance().getReturnServices().getServiceList().size();
        int numOfServiceTypes = ReturnServiceClassHolder.getInstance().getReturnServices().getServiceTypeList().size();

        for(int companyNum = 0; companyNum < numOfCompanies; companyNum ++){
            for(int serviceNum = 0; serviceNum < numOfServices; numOfServices++ ){
                for(int serviceTypeNum = 0; serviceTypeNum < numOfServiceTypes; serviceTypeNum ++){

                    returnedList.add(returnServices.getCompanyList().get(numOfCompanies));
                    returnedList.add(returnServices.getServiceList().get(numOfServices));
                    returnedList.add(returnServices.getServiceTypeList().get(numOfServiceTypes));
                    displayList.add(returnedList);
                }
            }
        }

        return  displayList;
    }
}
