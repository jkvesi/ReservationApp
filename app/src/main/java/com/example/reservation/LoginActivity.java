package com.example.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


import com.example.reservation.classes.ReturnServiceClassHolder;
import com.example.reservation.classes.ReturnServicesClass;
import com.example.reservation.classes.UserDataClass;
import com.example.reservation.functions.RetrieveDataFromDatabaseClass;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.reservation.classes.UserDataHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, backText;
    String firstName, lastName, country, city, phone, userEmail;
    Boolean switcher;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference reference;


   /* @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        editTextEmail = findViewById(R.id.emailLoginHolder);
        editTextPassword = findViewById(R.id.passLoginHolder);

    }
    public void onBackClick(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onLoginClick(View view) {

        String[] userName = new String[1];
        String email, password;
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(LoginActivity.this, "Email field is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "Password field is required", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login successfully",
                                    Toast.LENGTH_SHORT).show();
                            //////////////
                            mAuth = FirebaseAuth.getInstance();
                            user = mAuth.getCurrentUser();
                            reference = FirebaseDatabase.getInstance().getReference("Users");
                            if(user != null){
                                String userEmail = user.getEmail();
                                reference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {

                                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                            firstName = userSnapshot.child("firstName").getValue(String.class);
                                            lastName = userSnapshot.child("lastName").getValue(String.class);
                                            country = userSnapshot.child("country").getValue(String.class);
                                            city = userSnapshot.child("city").getValue(String.class);
                                            phone = userSnapshot.child("phone").getValue(String.class);
                                            switcher = userSnapshot.child("switcher").getValue(Boolean.class);
                                            //userEmail = userSnapshot.child("email").getValue(String.class);

                                            UserDataClass userData = new UserDataClass(firstName, lastName, switcher,phone, country,city, email);
                                            UserDataHolder.getInstance().setUserData(userData);

                                        }
                                    }


                                    public void onCancelled(@NonNull final DatabaseError error) {

                                    }
                                });
                            }

                            /////////////
                            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                            intent.putExtra("greeting", "Welcome" );
                            intent.putExtra("login", true);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}