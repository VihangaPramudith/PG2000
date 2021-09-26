package com.example.ficurred;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";

    boolean userFound;

    SharedPreferences sharedPreferences;
    String s_emailAddress;

    EditText emailAddress, password;
    RadioGroup options;
    RadioButton option;
    Manager manager;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        manager = new Manager();

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        s_emailAddress = sharedPreferences.getString(EMAIL_KEY, null);
    }

    public void RedirectPage(View view) {
        if (((CheckBox) findViewById(R.id.agreement_chk)).isChecked()) {

            emailAddress = findViewById(R.id.sign_up_email_address);
            password = findViewById(R.id.sign_up_password);

            options = findViewById(R.id.sign_up_role);
            int selectedOption = options.getCheckedRadioButtonId();
            option = findViewById(selectedOption);

            if (option.getText().toString().equals("Restaurant Manager")) {

                dbRef = FirebaseDatabase.getInstance().getReference();

                if (TextUtils.isEmpty(emailAddress.getText().toString()))
                    Toast.makeText(getApplicationContext(), "Please Enter an Email Address", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(password.getText().toString()))
                    Toast.makeText(getApplicationContext(), "Please Enter a Password", Toast.LENGTH_SHORT).show();
                else {

                    DatabaseReference dbChk = FirebaseDatabase.getInstance().getReference().child("Manager");

                    dbChk.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            userFound = true;
                            Manager manager_chk;

                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                                manager_chk = dataSnapshot.getValue(Manager.class);
                                if (manager_chk.getEmailAddress().equals(emailAddress.getText().toString())) {
                                    userFound = false;
                                    break;
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    if (userFound) {

                        manager.setEmailAddress(emailAddress.getText().toString());
                        manager.setPassword(password.getText().toString());

                        dbRef.child("Manager").push().setValue(manager);

                        Toast.makeText(getApplicationContext(), "Your Profile have been successfully created", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(EMAIL_KEY, emailAddress.getText().toString());

                        editor.apply();

                        Intent intent = new Intent(this, ManagerDashboard.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(getApplicationContext(), "Email Address Already Exists", Toast.LENGTH_SHORT).show();
                }
            }

        } else
            Toast.makeText(getApplicationContext(), "Please agree with our Terms of Services and Privacy Policy", Toast.LENGTH_SHORT).show();
    }
}