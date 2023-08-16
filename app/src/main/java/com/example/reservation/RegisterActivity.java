package com.example.reservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.reservation.classes.UserDataClass;
import com.example.reservation.functions.MapDataToDatabaseClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    String country ="";
    String city = "";
    FirebaseDatabase database;
    DatabaseReference reference;
    EditText editTextEmail, editTextPassword;
    EditText editTextFirstName, editTextLastName, editTextPhoneNumber;
    Switch switcher;
    EditText spinnerCountry, spinnerCity;

    FirebaseAuth mAuth;
    FrameLayout frame;

    MapDataToDatabaseClass mapCountryAndCity = new MapDataToDatabaseClass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.emailHolder);
        editTextPassword = findViewById(R.id.passHolder);
        editTextFirstName = findViewById(R.id.nameHolder);
        editTextLastName = findViewById(R.id.lastNameHolder);
        editTextPhoneNumber = findViewById(R.id.phoneHolder);
        switcher = findViewById(R.id.switcher);
        spinnerCountry = findViewById(R.id.countryHolder);
        spinnerCity = findViewById(R.id.cityHolder);
        switcher = findViewById(R.id.switcher);
        frame = findViewById(R.id.frame);



        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    frame.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    frame.requestLayout();
                } else {
                    frame.getLayoutParams().height = 0;
                    frame.requestLayout();
                }
            }
        });


     /*   ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dropdown_countries, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                country = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(this, R.array.dropdown_states, android.R.layout.simple_spinner_item );
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapterCity);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                city = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) {

            }
        });
*/
    }


    public void onFinishRegistrationClick(View v) {
        String email, password, firstName, lastName, phone; // country, city;
        Boolean switchOn;
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        firstName = editTextFirstName.getText().toString();
        lastName = editTextLastName.getText().toString();
        phone = editTextPhoneNumber.getText().toString();
        country = spinnerCountry.getText().toString();
        city = spinnerCity.getText().toString();
        switchOn = switcher.isChecked();

        DatabaseReference countryReference = database.getReference("Countries");
        mapCountryAndCity.mapCountryAndCityToDatabase(country,city, countryReference);

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Email field is required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Password field is required", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Account created successfully!",
                                    Toast.LENGTH_SHORT).show();
                            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                                UserDataClass user = new UserDataClass(firstName, lastName, switchOn, phone, country, city, email);
                                reference = database.getReference("Users");
                                reference.child(firstName).setValue(user);

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                editTextEmail.setText("");
                                editTextPassword.setText("");
                            } else {
                                Toast.makeText(RegisterActivity.this, "First and last name are required fields!",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegisterActivity.this, "User with that email address already exists!",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}