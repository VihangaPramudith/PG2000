package com.example.ficurred;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminSignIn extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";

    boolean userFound;
    Admin admin;

    SharedPreferences sharedPreferences;
    String s_emailAddress;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin_sign_in);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        s_emailAddress = sharedPreferences.getString(EMAIL_KEY, null);

        admin = new Admin();
    }

    public void signIn(View view) {
        EditText emailAddress = findViewById(R.id.admin_email_address);
        EditText password = findViewById(R.id.admin_signIn_password);

        if (TextUtils.isEmpty(emailAddress.getText().toString()))
            Toast.makeText(getApplicationContext(), "Please Enter an Email Address", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(password.getText().toString()))
            Toast.makeText(getApplicationContext(), "Please Enter a Password", Toast.LENGTH_SHORT).show();
        else {

            dbRef = FirebaseDatabase.getInstance().getReference().child("Admin");
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        admin = dataSnapshot.getValue(Admin.class);
                        if ((admin.getEmailAddress().equals(emailAddress.getText().toString())) && admin.getPassword().equals(password.getText().toString())) {
                            userFound = true;
                            break;

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }

            });

            if (userFound) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(EMAIL_KEY, emailAddress.getText().toString());

                editor.apply();

                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Email Address or Password Incorrect", Toast.LENGTH_SHORT).show();
        }
    }
}